<template>
  <div class="home-root-css" ref="root">
    <waterfall-tabs ref="waterfallTabs" @changeBgPlayerLevel="changeBgPlayerLevel">
      <template #buttonsHeader>
        <top-btns-view :logo-right="true" down-sid="tabNavBarSid" :visible="bgPlayerLevel == 'after'">
        </top-btns-view>
      </template>
    </waterfall-tabs>
  </div>
</template>

<script lang="ts">
import { useESEventBus, useESLog, ESKeyEvent } from "@extscreen/es3-core"
import {defineComponent} from "@vue/runtime-core";
import {ref} from "vue";
import TopBtnsView from "../../components/top-btns-view.vue";
import WaterfallTabs from "./components/waterfall-tabs.vue";
import ImgTextBtnView from "../../components/img-text-btn-view.vue";

export default defineComponent({
  name: "home",
  components: {
    'waterfall-tabs':WaterfallTabs,
    'img-text-btn-view':ImgTextBtnView,
    'top-btns-view':TopBtnsView
  },
  props:{
    height:{
      type:String,
      default: "80px"
    }
  },
  setup(props, context) {
    const waterfallTabs = ref()
    const esEventBus = useESEventBus()
    let bgPlayerLevel = ref('after')
    let ESLog = useESLog()
    let TAG = "DebugNestPlayer"
    function onESCreate(app : any,params:any) {
      ESLog.i(TAG,"onESCreate params:"+JSON.stringify(params))
      waterfallTabs.value?.onESCreate(params)
    }

    function onESNewIntent(intent : any){
      ESLog.i(TAG,`onESNewIntent intent:${JSON.stringify(intent)}`)
      waterfallTabs?.value.reloadAll()
    }

    function onESRestart(){
      ESLog.i(TAG,"onESRestart ")
      waterfallTabs.value?.onESRestart()
    }

    function onESStart() {
      ESLog.i(TAG,"onESStart ")
    }

    function onESPause() {
      ESLog.i(TAG,"onESPause ")
      waterfallTabs.value?.onESPause()
    }

    function onESResume() {
      ESLog.i(TAG,"onESResume ")
      esEventBus.emit("bg-player-life-cycle","onESResume")
      waterfallTabs.value?.onESResume()
    }

    function onESStop() {
      ESLog.i(TAG,"onESStop ")
      esEventBus.emit("bg-player-life-cycle","onESStop")
      waterfallTabs.value?.onESStop()
    }

    function onESDestroy() {
      ESLog.i(TAG,"onESDestroy ")
      esEventBus.emit("bg-player-life-cycle","onESDestroy")
      waterfallTabs.value?.onESDestroy()
    }

    const onKeyDown = (keyEvent: ESKeyEvent) => {
      waterfallTabs.value?.onKeyDown(keyEvent)
    }
    const onKeyUp = (keyEvent: ESKeyEvent) => {
      waterfallTabs.value?.onKeyUp(keyEvent)
    }

    function onBackPressed() {
      ESLog.e(TAG,"onBackPressed ")
      waterfallTabs.value?.onBackPressed()
    }

    function changeBgPlayerLevel(type:string) {
      bgPlayerLevel.value = type
    }

    return {
      waterfallTabs,bgPlayerLevel,changeBgPlayerLevel,
      onESCreate,
      onESStart,
      onESResume,
      onESStop,
      onESPause,
      onESDestroy,
      onESRestart,
      onKeyDown,
      onKeyUp,
      onBackPressed,
      onESNewIntent
    }
  }
})
</script>

<style src="./css/home.css">

</style>
