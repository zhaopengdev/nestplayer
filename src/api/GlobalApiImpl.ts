import {ESPlayerDefinition} from "@extscreen/es3-player"
import FilterConfig from "../pages/filter/build_data/FilterConfig";
import bg_play from "./home/mock/bg_play";
import {IGlobalApi} from "./IGlobalApi";
import {RequestManager} from "./request/RequestManager";
import {
  QTTab,
  QTTabItem,
  QTTabItemType,
  QTTabPageData,
  QTTabPageState,
  QTWaterfallItem,
  QTWaterfallSection,
  QTWaterfallSectionType
} from "@quicktvui/quicktvui3";
import {Tab} from "../pages/home/build_data/tab/impl/Tab";
import tabMockJson from "./home/mock/home_tab";
import {
  buildTransferTabAdapter,
  getTabBackground,
} from "../pages/home/build_data/tab/TabTransferAdapter";
import tabPage0MockJson from "./home/mock/home_page0";
import tabPage1MockJson from "./home/mock/home_page1";
import tabPage2MockJson from "./home/mock/home_page2";
import tabPage3MockJson from "./home/mock/home_page3";
import {
  buildTransferTabContentAdapter
} from "../pages/home/build_data/tab_content/TabContentTransferAdapter";
import {ESApp} from "@extscreen/es3-vue";
import {GlobalApiKey} from "./UseApi";
import BuildConfig from "../build/BuildConfig";
import {filterContentUrl, filterEntryUrl, hotSearchUrl,} from "./RequestUrl";

/*****
 ***************搜索 *********
 *****/
import {
  buildSearchCenterListData,
  buildSearchResultData,
  buildSearchTabData,
} from "../pages/search/build_data/useSearchData";
import searchCenterList from "./search/mock/search_center_list";
import searchResultTabList from "./search/mock/search_result_tab_list";
import searchRecommendTabList from "./search/mock/search_recommend_tab";
import searchResultPageData from "./search/mock/search_result_page_data";
import searchResultPageData2 from "./search/mock/search_result_page_data2";
import searchRecommendResultData from "./search/mock/search_result_recommend_data";
import SearchConfig from "../pages/search/build_data/SearchConfig";
import {SearchCenter} from "../pages/search/build_data/impl/SearchCenter";
import {SearchTab} from "../pages/search/build_data/impl/SearchTab";
import {SearchResult} from "../pages/search/build_data/impl/SearchResult";

/***** *************** 短视频 **************/
import {
  buildMultilevelTabItemAdapter,
  buildShortVideoItemAdapter
} from "../pages/shortVideo/build_data/adapter";
import shortVideoList from "./shortVideo/mock/short_video_data";
import {leftExpand, leftTags} from "./filter/mock";
import {buildQTTab} from "../pages/home/build_data/tab/TabAdapter";
import {searchFilesByType} from "../utils/client";
import {ESLog, useESLog} from "@extscreen/es3-core";

const log : ESLog = useESLog()
const TAG = "DebugNestPlayer"

export function createGlobalApi(): IGlobalApi {
  let requestManager: RequestManager;



  function init(...params: any[]): Promise<any> {
    requestManager = params[0];
    return Promise.resolve();
  }

  function getTabList(): Promise<QTTab> {
    //使用本地数据
    const tabs: Array<QTTabItem> = []
    const musicTab : QTTabItem = {
      _id: 'fav',
      type: QTTabItemType.QT_TAB_ITEM_TYPE_DEFAULT,
      text: '收藏',
      titleSize: 36,
      decoration: {}
    }

    const video : QTTabItem = {
      _id: 'video',
      type: QTTabItemType.QT_TAB_ITEM_TYPE_DEFAULT,
      text: '视频',
      titleSize: 36,
      decoration: { }
    }

    const image : QTTabItem = {
      _id: 'image',
      type: QTTabItemType.QT_TAB_ITEM_TYPE_DEFAULT,
      text: '图片',
      titleSize: 36,
      decoration: { }
    }
    tabs.push(musicTab)
    tabs.push(video)
    // tabs.push(image)
    let tab = buildQTTab(1,1,tabs);
    return Promise.resolve(tab);
  }

  function getMockTabList(): Promise<QTTab> {
    const tabs: Array<Tab> = tabMockJson as Array<Tab>;
    return Promise.resolve(buildTransferTabAdapter(tabs));
  }

  function getHistoryItems(): Array<QTWaterfallItem> {
    let items : Array<QTWaterfallItem> = []
    for(let i = 0; i < 4; i++) {
      let item : QTWaterfallItem = {
        _id: i.toString(),
        type: 20000,
        title: `视频名称第一季第${i+1}集1080p.mp4`,
        subTitle: '2024/12/12',
        videoUrl: 'http://www.w3school.com.cn/i/movie.mp4',
        duration:500*1000,
        progress:100000,
        size:[392,392],
        style:{
          width:392,
          height:392
        },
        decoration: {
          left: i == 0 ? 0 : 40,
        }
      }
      items.push(item)
    }
    return items
  }

  function getRealVideos(): Promise<Array<QTWaterfallItem>>{
    // ESLog.i(TAG,"getRealVideos called")
    searchFilesByType(['mp4','mkv','avi','flv','webM','3gp'],100,'').then((res) => {

      console.log(`searchFilesByType result:${JSON.stringify(res)}`)
      if(res.code == 200){
        // ESLog.i(TAG,"getRealVideos result:",res.length)
        let files = res.data
        let items : Array<QTWaterfallItem> = []
        for(let i  = 0;i < files.length; i++) {
          let ri = files[i]
          let item : QTWaterfallItem = {
            _id: i.toString(),
            type: 20000,
            title: ri.fileName,
            subTitle: ri.createTime,
            videoUrl: ri.url,
            size:[392,392],
            style:{
              width:392,
              height:392
            },
            decoration: {
              left: i == 0 ? 0 : 40,
            }
          }
          items.push(item)
        }
        return Promise.resolve(items)
      }else{
        return Promise.reject(res.message)
      }
    }).catch((err) => {
      console.error(err)
      // ESLog.e(TAG,"getRealVideos err:"+err.toString())
    })
    // ESLog.e(TAG,"getRealVideos reject");
    return Promise.reject('')
  }

  function getMockVideos() : Promise<Array<QTWaterfallItem>> {
    let items : Array<QTWaterfallItem> = []
    for(let i = 0; i < 100; i++) {
      let item : QTWaterfallItem = {
        _id: i.toString(),
        type: 20000,
        title: `视频名称第一季第${i+1}集1080p.mp4`,
        subTitle: '2024/12/12',
        videoUrl: 'http://www.w3school.com.cn/i/movie.mp4',
        duration:500*1000,
        progress:100000,
        size:[392,392],
        style:{
          width:392,
          height:392
        },
        decoration: {
          right:40,
        }
      }
      items.push(item)
    }
    return Promise.resolve(items)
  }

  function getTabContent(
    tabId: string,
    pageNo: number,
    pageSize: number,
    tabPageIndex?: number
  ): Promise<QTTabPageData> {
    // ESLog.e(TAG,`getTabContent tabId:${JSON.stringify(tabId)}`)
    let sections:QTWaterfallSection[] = []
    let tabContentData: QTTabPageData = {
      state: pageNo > 0 ?  QTTabPageState.QT_TAB_PAGE_STATE_COMPLETE : QTTabPageState.QT_TAB_PAGE_STATE_COMPLETE,
      data:sections,
      isEndPage : true,
      disableScrollOnFirstScreen:true,
    };
    if(pageNo > 1){
      return  Promise.resolve(tabContentData);
    }
    // client.searchFilesByType('music','mp3',1,10).then((res) => {
    let historySection : QTWaterfallSection = {
      _id: 'history',
      title: '历史记录',
      titleStyle:{
        width: 1000,
        height:60
      },
      type: QTWaterfallSectionType.QT_WATERFALL_SECTION_TYPE_LIST,
      itemList: getHistoryItems(),
      decoration:{
        top: 560,
        left:116
      },
      style:{
        width: 1920,
        height: 392+48 + 60,
      }
    }
    sections.push(historySection)
    console.log(`getTabContent tabId:${tabId}`)
    // log.i('DebugNestPlayer',`getTabContent tabId:${tabId}`)
    // getMockVideos().then((res) => {
    if(tabId == 'video') {
      getRealVideos().then((res) => {
        console.log(`itemList historySection res length:${res.length}`)
        // ESLog.i(TAG,`itemList historySection res length:${res.length}`)
        //historySection.itemList = res
        let gridSectionList = buildGridSectionList(res,4)
        gridSectionList[0].decoration!.bottom = gridSectionList[0].decoration!.bottom! + 40;
        gridSectionList[0].decoration!.top = 48;
        gridSectionList[0].titleStyle = {
          width: 1000,
          height:60
        }
        gridSectionList[0].title = '全部视频'
        sections.push(...gridSectionList)
        return Promise.resolve(tabContentData);
      }).catch((err) => {
        let tabContentData: QTTabPageData = {
          state: 0,
          data:sections
        };
        return Promise.resolve(tabContentData);
      })
    }else{
      getMockVideos().then((res) => {
        console.log(`itemList historySection res length:${res.length}`)
        //historySection.itemList = res
        let gridSectionList = buildGridSectionList(res,4)
        gridSectionList[0].decoration!.bottom = gridSectionList[0].decoration!.bottom! + 40;
        gridSectionList[0].decoration!.top = 48;
        gridSectionList[0].titleStyle = {
          width: 1000,
          height:60
        }
        gridSectionList[0].title = '全部视频'
        sections.push(...gridSectionList)
        return Promise.resolve(tabContentData);
      }).catch((err) => {
        let tabContentData: QTTabPageData = {
          state: 0,
          data:sections
        };
        return Promise.resolve(tabContentData);
      })
    }

    return Promise.resolve(tabContentData)
  }

  function buildGridSectionList(items:Array<QTWaterfallItem>,span :number): Array<QTWaterfallSection> {
      let count = items.length % span == 0 ? items.length / span : items.length / span + 1;
      let sections = new Array<QTWaterfallSection>();
      let start = 0
      for(let i = 0; i < count; i++) {
          let section : QTWaterfallSection =  {
              itemList: [],
              style:{
                width: 1920,
                height: 392,
              },
              decoration:{
                bottom:40,
                left:116
              },
              type: QTWaterfallSectionType.QT_WATERFALL_SECTION_TYPE_LIST
            }
            sections.push(section)
           for(let j = 0; j < span;j ++){
             let index = j + start;
             if(index < items.length ){
               section.itemList.push(items[index]);
             }
           }
          start += span;
      }
      return sections;
  }

  function getMockTabContent(
    tabId: string,
    pageNo: number,
    tabPageIndex?: number
  ): Promise<QTTabPageData> {
    const name: Array<any> = [
      tabPage0MockJson,
      tabPage1MockJson,
      tabPage2MockJson,
      tabPage3MockJson,
    ];
    const index = Number(tabId);
    return Promise.resolve(
      buildTransferTabContentAdapter(name[index], pageNo, tabId, tabPageIndex)
    );
  }

  function getTabBg(tabId): string {
    return getTabBackground(tabId);
  }

  function getHomeBgVideoAssetsUrl(id: string): Promise<object> {
    //todo 实现获取播放地址接口
    const urls = bg_play;
    return Promise.resolve({
      url: urls[id],
      definition:ESPlayerDefinition.ES_PLAYER_DEFINITION_SD
    });
  }

  //***************************************************搜索相关***************
  function getHotSearch(pageNum: number, keyword?: string): Promise<SearchCenter> {
    if (BuildConfig.useMockData) {
      let list: Array<any> = [];
      if (searchCenterList.keywordList.length > 0) list = searchCenterList.keywordList;
      const isLoadHistory = searchCenterList.historyList.length > 0;
      if (isLoadHistory) list = searchCenterList.historyList;
      return Promise.resolve(buildSearchCenterListData(list, isLoadHistory));
    }
    // 根据keyword字母搜索关键字 不传返回热门搜索
    return requestManager
      .post(hotSearchUrl, {
        data: keyword,
        param: { pageNo: pageNum, pageSize: SearchConfig.searchCenterPageSize },
      })
      .then((result: any) => {
        let list: Array<any> = [];
        if (result.keywordList.length > 0) list = result.keywordList;
        if (result.historyList.length > 0) list = result.historyList;
        return buildSearchCenterListData(list, result.historyList.length > 0);
      });
  }

  function clearHistory(): void {}

  function getSearchResultTabList(isHotRecommend: boolean): Promise<Array<QTTabItem>> {
    //此处可更换接口请求数据
    if (BuildConfig.useMockData || true) {
      if (isHotRecommend) {
        return Promise.resolve(buildSearchTabData(searchRecommendTabList as Array<SearchTab>));
      } else {
        return Promise.resolve(buildSearchTabData(searchResultTabList as Array<SearchTab>));
      }
    }
  }

  function getSearchResultPageData(
    tabId: string,
    pageNo: number,
    pageSize: number,
    singleTab: boolean
  ): Promise<QTTabPageData> {
    //此处可更换接口请求数据
    if (BuildConfig.useMockData || true) {
      if (pageNo === 20) {
        //模拟结束
        return Promise.resolve(buildSearchResultData({ itemList: [] }, pageNo, singleTab));
      }
      const result = pageNo === 19 ? searchResultPageData2 : searchResultPageData;
      return Promise.resolve(buildSearchResultData(result as SearchResult, pageNo, singleTab));
    }
  }

  function getRecommendPageData(
    tabId: string,
    pageNo: number,
    pageSize: number,
    singleTab: boolean
  ): Promise<QTTabPageData> {
    //此处可更换接口请求数据
    if (BuildConfig.useMockData || true) {
      if (pageNo === 3) {
        //模拟结束
        return Promise.resolve(buildSearchResultData({ itemList: [] }, pageNo, singleTab));
      }
      const result = pageNo === 1 ? searchRecommendResultData : searchResultPageData2;
      return Promise.resolve(buildSearchResultData(result as SearchResult, pageNo, singleTab));
    }
  }

  /********************************筛选相关*****************************/
  function getScreenLeftExpand(): Promise<any> {
    return Promise.resolve(leftExpand);
  }

  function getScreenLeftTags(screenId: string) {
    return Promise.resolve(leftTags[screenId] ?? {});
    const requestUrl = filterEntryUrl + screenId;
    return requestManager.post(requestUrl, {});
  }

  function getScreenContentByTags(tags, pageNum) {
    const params = requestManager.getParams();
    const pageParams = {
      pageNo: pageNum,
      pageSize: FilterConfig.screenPageSize,
    };
    const newParams = { ...params, ...pageParams };
    return requestManager.post(filterContentUrl, {
      param: newParams,
      data: tags,
    });
  }

  /********************************短视频相关*****************************/
  function getShortVideoPageData(keyword: string, pageNo: number, pageSize: number): Promise<Array<QTWaterfallItem>> {
    //此处可更换接口请求数据
    if (BuildConfig.useMockData || true) {
      if (pageNo > 2) {
        //模拟结束
        return Promise.resolve(buildShortVideoItemAdapter([]));
      }
      return Promise.resolve(buildShortVideoItemAdapter(shortVideoList));
    }
  }

  /********************************多级Tab相关*****************************/
  function getMultilevelTabPageData(keyword: string, pageNo: number, pageSize: number): Promise<Array<QTWaterfallItem>> {
    console.log('huan-xxx', pageNo, pageSize)
    //此处可更换接口请求数据
    if (BuildConfig.useMockData || true) {
      if (pageNo > 2) {
        //模拟结束
        return Promise.resolve(buildMultilevelTabItemAdapter([]));
      }
      return Promise.resolve(buildMultilevelTabItemAdapter(shortVideoList.slice((pageNo-1)*pageSize, pageNo*pageSize)));
    }
  }

  return {
    install: function (app: ESApp) {
      const instance = this;
      app.provide(GlobalApiKey, instance);
    },
    init,
    getTabList,
    getTabContent,
    getTabBg,
    getHomeBgVideoAssetsUrl,
    getHotSearch,
    clearHistory,
    getSearchResultTabList,
    getSearchResultPageData,
    getRecommendPageData,
    getScreenLeftExpand,
    getScreenLeftTags,
    getScreenContentByTags,
    getShortVideoPageData,
    getMultilevelTabPageData
  };
}
