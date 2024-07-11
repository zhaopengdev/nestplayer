import { rankingTypes, IposterConfig } from '../../api/ranking/types';
import type { IrankingTabItem, IrankingContent, IrankingMoreContent,IrankingConfig, IrankingContentItem } from '../../api/ranking/types';
import type {
  QTTabItem, QTITab, QTWaterfallSection,QTWaterfallItem
} from "@quicktvui/quicktvui3";
import { VirtualView, QTWaterfallSectionType, QTWaterfallItemType } from "@quicktvui/quicktvui3";
import rankApi from '../../api/ranking/index';
import { Ref } from 'vue'
import { Native } from '@extscreen/es3-vue';
// import numImg1 from '../../assets/ranking/1.png'
// import numImg2 from '../../assets/ranking/2.png'
// import numImg3 from '../../assets/ranking/3.png'
// import numImg4 from '../../assets/ranking/4.png'
// import numImg5 from '../../assets/ranking/5.png'
// import numImg6 from '../../assets/ranking/6.png'
// import numImg7 from '../../assets/ranking/7.png'
// import numImg8 from '../../assets/ranking/8.png'
// import numImg9 from '../../assets/ranking/9.png'
// import numImg10 from '../../assets/ranking/10.png'

export const pageWidth = 1920
export const pageHeight = 1080

/**
 * tab栏数据转换函数
 */
export const transRankingTabList = (arr:IrankingTabItem[], configs:IrankingConfig):QTTabItem[] => {
  return arr.map((item,index) => {
    return { 
      _id: (item.id||'tab')+index,text: item.text, type: 1, titleSize: 30, 
      decoration: {
        left: index===0 ? configs.pageSpace - 25 : 0,
        right: index===arr.length-1 ? configs.pageSpace : 0
      } 
    }
  })
}

export const rankingContentTypes = {
  info: 1, sort: 2, more: 3
} as const;

const dPosterWidth = 396
const dPosterHeight = 222
const drightSpace = 48
const getRankingPoster = (pData:IrankingContentItem, config:IposterConfig, index?:number) => {
  const imgW = config.posterImgWidth||config.posterWidth||dPosterWidth
  const imgH = config.posterImgHeight||config.posterHeight||dPosterHeight
  return {
    _id: 'rankingSectionPoster-' + pData.id, type: config.posterType||QTWaterfallItemType.QT_WATERFALL_ITEM_TYPE_POSTER,
    focus: { enable: true, scale: 1.1, border: true },
    rwaData: { ...pData },
    decoration: {
      left: 0,
      right: config.rightSpace||drightSpace
    },
    style: {
      width: config.posterWidth||dPosterWidth,
      height: config.posterHeight||dPosterHeight,
    },
    image: {
      src: pData.poster, enable: true,
      style: {
        width: imgW,
        height: imgH,
      }
    },
    title: {
      text: '',
      enable: false,
      style: {}
    },
    subTitle: {
      text: '',
      enable: false,
      style: {}
    },
    floatTitle: {
      text: '',
      enable: false,
      style: {},
    },
    shimmer: {
      enable: false,
    },
    ripple: {
      enable: false,
      style: {}
    },
    corner: {
      text: '',
      enable: false,
      style: {},
      background: {}
    },
    titleStyle: { },
    titleFocusStyle: { },
    numImg: {
      url: (index||index===0)?require(`../../assets/ranking/${index+1}.png`).default:undefined,//`../../../assets/ranking/1.png`,
      style: {
        width: 224,
        height: 308,
        marginTop: 40,
        marginLeft: -50
      }
    },
    coverStyle: {
      width: imgW + 12,
      height: imgH + 12
    }
  }
}
const getCurrentSection = (current:IrankingContentItem, data?:IrankingContent, configs?:IrankingConfig, oldCurrent?:{[k:string]:any}) => {
  let tagStrText = ''
  if(current.tagStr){
    tagStrText += `<font>${current.tagStr}</font> `
  }
  if(current.score){
    tagStrText += `<font color="#FF9F0A" size="20">${current.score}</font>`
  }
  const rankName = data?.rankName||oldCurrent?.rankName.text
  return {
    _id: oldCurrent?._id || 'rankingCurrentSection-'+data?.id,
    type: rankingContentTypes.info,
    title: '',
    titleStyle: {
      width: 0.01,
      height: 0.01,
      marginLeft: 0.01,
      marginTop: 0.01,
      marginBottom: 0.01,
    },
    decoration: oldCurrent?.decoration || { top: 130, left: configs?.pageSpace||0 },
    style: oldCurrent?.style || {
      width: pageWidth - (configs?.pageSpace||0), height: 650,
    },
    itemList:[],
    vcTitle: {
      text: current.title,
      style: {
        height: current.title?58:0,
        marginTop: current.title?150:0
      }
    },
    vcSubTitle: {
      text: current.subTitle,
      style: {
        height: current.subTitle?40:0,
        marginTop: 0
      }
    },
    vcTitleImg: {
      url: current.titleImg,
      style:  {
        height: current.titleImg?150:0,
        marginTop: current.titleImg?100:0
      }
    },
    des: {
      text: current.des,
      style: {
        height: current.des?90:0,
        marginTop: current.des?15:0
      }
    },
    rankName: {
      text: rankName,
      style: {
        height: rankName?46:0,
        marginTop: rankName?30:0
      }
    },
    tagStr: {
      text: tagStrText,
      style: {
        height: tagStrText?50:0,
        marginTop: tagStrText?20:0
      }
    },
    isVedio: !current.previewImg,
    previewImg: current.previewImg,
    previewVedio: current.previewVedio,
    bgTags: (current.bgTags||[]).map(item=>{
      return { ...item, type: 101 }
    })
  }
}

export const transRankingContent = (data:IrankingContent, configs:IrankingConfig) => {
  const itemList = data.list.map((item,index)=>{
    return getRankingPoster(item, {
      posterWidth: 320, posterHeight: 348,
      posterImgWidth: 246, posterImgHeight: 348,
      rightSpace: 70, posterType: 1,
      ...(data.config||{})
    }, index)
  })
  const sections:QTWaterfallSection[] = [
    getCurrentSection(itemList[0].rwaData, data, configs),
    {
      _id: 'rankingSortSection-'+data.id, type: rankingContentTypes.sort,
      title: '',
      titleStyle: {
        width: 0.01,
        height: 0.01,
        marginLeft: 0.01,
        marginTop: 0.01,
        marginBottom: 0.01,
      },
      decoration: { top: -150, left: configs.pageSpace },
      style: { width: pageWidth - configs.pageSpace, height: 350, },
      itemList
    }
  ]
  return { sections }
}
export const transRankingMoreContent = (data:IrankingMoreContent, configs:IrankingConfig) => {

  const itemList = data.moreList.map((item,index) => {
    const isFirst = index === 0
    return {
      _id: item.id+index,
      type: QTWaterfallSectionType.QT_WATERFALL_SECTION_TYPE_LIST,
      title: isFirst?'':item.rankName,
      titleStyle: {
        width: 1000,
        height: isFirst?0.1:52,
        marginTop: isFirst?0.1:40,
        marginBottom: isFirst?0.1:20,
        fontSize: isFirst?0.1:42
      },
      itemList: item.list.map((mlItem,mlIndex)=>{
        return getRankingPoster(mlItem, item.config||{})
      }),// getRankingPoster(i + flag, 5),
      style: {
        width: pageWidth - configs.pageSpace,
        height: 0.01,
      },
      decoration: {
        left: 0.01, right: 0.01, top: 0.01
      }
    }
  })
  const sections:QTWaterfallSection[] = [
    getCurrentSection(data.moreList[0].list[0], data.moreList[0], configs),
    {
      _id: 'rankingMoreSection-'+data.id, type: rankingContentTypes.more,
      title: '',
      titleStyle: {
        width: 0.01,
        height: 0.01,
        marginLeft: 0.01,
        marginTop: 0.01,
        marginBottom: 0.01,
      },
      decoration: { top: data.topSpace, left: configs.pageSpace },
      style: { width: pageWidth - configs.pageSpace, height: 384, },
      itemList
    }
  ]
  return { sections }
}

export const transRankingSections = (data:IrankingContent|IrankingMoreContent, configs:IrankingConfig) => {
  if(data.type === rankingTypes.sort){
    return transRankingContent(data as IrankingContent, configs)
  } else {
    return transRankingMoreContent(data as IrankingMoreContent, configs)
  }
}

class RankingUi {
  private pageIndex:number = -1
  private tabRef?:QTITab

  updateCurrent(rwaData:IrankingContentItem){
    if(this.pageIndex>-1){
      console.log(this.pageIndex, '--this.pageIndex')
      const oldSectin = this.tabRef?.getPageSection(this.pageIndex, 0);
      const newSection = getCurrentSection(rwaData, undefined, undefined, oldSectin)
      if(oldSectin?._id){
        VirtualView.updateChild('rankingTabsSid',oldSectin?._id, newSection)
      }
    }
  }

  updateData(pageIndex){
    this.pageIndex = pageIndex
  }

  setData(tabRef:QTITab, pageIndex:number){
    rankApi.getContentData(pageIndex).then(res=>{
      const {sections} = transRankingSections(res, rankApi.getConfig())
      tabRef.setPageData(pageIndex, {
        useDiff: false, isEndPage: true, disableScrollOnFirstScreen: false,
        data: sections
      })
    })
    this.pageIndex = pageIndex
    this.tabRef = tabRef
  }
}

export const rankingUi = new RankingUi()