import type {
  QTWaterfallSection, QTWaterfallItem
} from "@quicktvui/quicktvui3";
import { QTWaterfallSectionType, QTWaterfallItemType } from "@quicktvui/quicktvui3";
import type { IselectionPoster, IselectionBaseSection, IselectionSection,ItabListItem,IvideoParams,IvideoDes,Id2BaseSection } from '../../api/details2/types'
import { tabTypes, posterTypes } from '../../api/details2/types'
import { getPosterConfig } from '../../components/Hposter/configs'
import d2Api from '../../api/details2/index'

export const D2SelectionsSectionTypes = {
  selection: 1, more: QTWaterfallSectionType.QT_WATERFALL_SECTION_TYPE_LIST
}

const sWaterfallWidth = 1920
const sWaterfallHeight = 470

const dSectionSpace = 96
const dSectionTitleSize = 42
const dSectionTitleBottom = 30

export const getSelectionPoster = (sData:IselectionPoster) => {
  const config = getPosterConfig({
    ...sData, titleLines: (sData.title||'').length>11?2:1
  })
  if(sData._type){
    config.type = sData._type
  }
  return {
    ...config,
    _router: sData._router,
    videoUrl: sData.videoUrl,
    videoData: sData.videoData
  }
}

export const ids = {
  selection: 'selection'
}
export const getSelectionSectionTabs = (data:ItabListItem) => {
  const type = data.type||tabTypes.text
  let right = 0
  if(tabTypes.btn){
    right = 20
  }
  if(tabTypes.smallText){
    right = 40
  }
  return {
    ...data, type,
    decoration: { right }
  }
}
export const getSelectionSection = (data:IselectionSection):QTWaterfallSection => {
  const space = data._config?.space || dSectionSpace
  detail2Ui.selectionSpace = space

  const section = getBlankSection(60, space-20)
  section._id = data.id
  section.listSID = ids.selection+data.id
  section.type = D2SelectionsSectionTypes.selection
  section.itemList = (data.tabList as any)||[]
  section.decoration.top = 10
  section.decoration.bottom = 25

  detail2Ui.tabSid = section.listSID

  return section
}
export const getSelectionMoreSection = (data:IselectionBaseSection):QTWaterfallSection => {
  const space = data._config?.space || dSectionSpace
  const titleSize = dSectionTitleSize
  const titleBottom = dSectionTitleBottom
  return {
    _id: ids.selection+data.id, type: D2SelectionsSectionTypes.more,
    title: data.title,
    titleStyle: {
      width: 1000,
      height: data.title?titleSize+10:0,
      fontSize: data.title?titleSize:0,
      marginBottom: data.title?titleBottom:0
    },
    decoration: { top: 0, left: space, right: 0, bottom: dSectionTitleBottom },
    style: { width: sWaterfallWidth - space, height: 330, },
    itemList: data.itemList
  }
}

export const getBlankSection = (height = 0, space = 0) => {
  return {
    _id: Math.random()+'', type: -1000, title: '',
    titleStyle: { width: 0, height: 0, fontSize: 0, marginBottom: 0 },
    decoration: { top: 0, left: space, right: 0, bottom: 0 },
    style: { width: sWaterfallWidth - space, height, minHeight: height },
    itemList:[],
    listSID: 'listSID'+Math.random()
  }
}

export type TposterType = ReturnType<typeof getSelectionPoster>;
export type TselectionTabType = ReturnType<typeof getSelectionSectionTabs>;

interface IpathLinked {
  index:number;
  next?:IpathLinked
}
class Detail2Ui {

  vdata?:IvideoDes// 详情页主视频信息
  playList:Id2BaseSection[] = []
  private monitors = new Set<(list:any[],vdata?:IvideoDes)=>void>()
  
  selectionSpace = 0

  private prevSelectTabIndex=-1
  private prevSelectTab2Index=-1
  private currentPlayPath:any[] = []
  selectTabIndex = 0
  selectTab2Index = 0
  selectTabListIndex = 0
  selectionPositoin = 0//选集的位置
  tabPath = new Map<number, IpathLinked>()

  tabSid = ''
  tab2Sid = ''
  tabListSid = ''

  /**
   * 设置初始详情页数据
   * @param vParams 
   */
  async setVideo(vParams: IvideoParams){
    const vData = await d2Api.getDetailVideoData(vParams)
    this.vdata = vData
    const selectionList = await d2Api.getMediaSelectionList(vData)
    this.playList = selectionList

    // 通过 vData.id 查询所在tab位置并设置初始位置
    this.selectTabIndex = 0
    this.selectTab2Index = 0
    this.selectTabListIndex = 0
    
    this.setTabPath()
  }

  setTabPath(){
    this.tabPath.set(this.selectTabIndex, {
      index: this.selectTabIndex,
      next: {
        index: this.selectTab2Index,
        next: {
          index: this.selectTabListIndex
        }
      }
    })
  }
  
  $on(fn:(list:Id2BaseSection[],vdata?:IvideoDes)=>void){
    this.monitors.add(fn)
  }
  $off(fn:(list:Id2BaseSection[],vdata?:IvideoDes)=>void){
    this.monitors.delete(fn)
  }
  $emit(){
    this.monitors.forEach(fn=>fn(this.playList,this.vdata))
  }

  /**
   * 切换视频
   */
  changeVideo(selectTabListIndex){
    const playPath = [this.selectTabIndex,this.selectTab2Index,selectTabListIndex]
    if(playPath.join()!=this.currentPlayPath.join()){

      this.selectTabListIndex = selectTabListIndex
      this.$emit()

      this.prevSelectTabIndex = this.selectTabIndex
      this.prevSelectTab2Index = this.selectTab2Index
      this.currentPlayPath = playPath
      this.setTabPath()
    }
  }
  
  isChangedTab(){
    return this.prevSelectTabIndex!==this.selectTabIndex || this.prevSelectTab2Index!==this.selectTab2Index
  }
  addPlayList(newList:any[] = []){
    this.playList = this.playList.concat(newList)
  }
  changePlayList(newList?:any[]){
    if(newList){
      this.playList = newList
    }
  }
  getCurrentPlay(){
    return this.playList[this.selectTabListIndex]
  }
  getTab2(tabItem:ItabListItem){
    const tabs2Section = detail2Ui.getShowTab(tabItem)
    
    let tabs2Item:any = null
    if(tabItem.tabList){
      tabs2Item = tabs2Section.itemList[detail2Ui.selectTab2Index]
    } else {
      tabs2Item = tabItem
    }
    const tab2ContentSection = detail2Ui.getShowTabList(tabs2Item as any)
    return { tabs2Section, tab2ContentSection }
  }

  getShowTab(data:ItabListItem){
    const section = getBlankSection(0, this.selectionSpace)
    section.type = D2SelectionsSectionTypes.selection

    if(data){
      section._id = data.id
      section.listSID = ids.selection+data.id
      let tabItem:ItabListItem|null = null
      if(data.tabList){
        tabItem = data.tabList[0]
        section.itemList = data.tabList as any
      }
      if(tabItem){
        if(tabItem.type === tabTypes.btn){
          section.style.height = 66
          section.style.minHeight = 66
          section.decoration.bottom = 15
        }
        if(tabItem.type === tabTypes.smallText){
          section.style.height = 40
          section.style.minHeight = 40
          section.decoration.bottom = 25
          section.decoration.left = this.selectionSpace-15
        }
      }
    }
    
    this.tab2Sid = section.listSID
    return section
  }
  getShowTabList(data:ItabListItem){
    const section = getBlankSection(0, this.selectionSpace)
    section.type = D2SelectionsSectionTypes.selection//1007 vue-section

    if(data){
      section._id = data.id
      section.listSID = ids.selection+data.id
      let listItem:TposterType|null = null
      if(data.itemList){
        listItem = data.itemList[0]
        section.itemList = data.itemList as any
      }
      if(listItem){
        if(listItem.type === posterTypes.bigBtn){
          section.style.height = 120
          section.style.minHeight = 120
        } else {
          section.style.height = listItem.style.height
          section.style.minHeight = listItem.style.height
        }
        section.decoration.bottom = dSectionTitleBottom
      }
    }
    if(data.isSelectionTab){
      this.selectionPositoin = this.selectTabIndex
      section.type = 1007
      section.decoration.left=0
      section.style.height = 270
      section.style.minHeight = 270
      this.tabListSid = section.listSID//todo
    } else {
      this.tabListSid = section.listSID
    }
    this.changePlayList(section.itemList)
    return section
  }
  clear(){
    this.monitors.clear()
    this.playList = []
    this.selectionSpace = 0

    this.selectTabIndex = 0
    this.selectTab2Index = 0
    this.selectTabListIndex = 0

    this.tabSid = ''
    this.tab2Sid = ''
    this.tabListSid = ''
    this.prevSelectTabIndex =-1
    this.prevSelectTab2Index =-1
    this.currentPlayPath = []
    this.vdata = undefined
    this.tabPath = new Map<number, IpathLinked>()
    this.selectionPositoin = 0
  }
}
export const detail2Ui = new Detail2Ui()