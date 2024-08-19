<template>
  <qt-view class="video_item" :focusable="true" :enableFocusBorder="true" :focusScale="1.05">
<!--      <qt-view class="video_item_img_placeholder" style="position: absolute" :focusable="false"/>-->
      <qt-image class="video_item_img" :duplicateParentState="true" :focusable="false"/>
      <qt-text text="${title}" class="video_item_title" :duplicateParentState="true" :focusable="false"/>
      <qt-text text="${subTitle}" class="video_item_subtitle" :duplicateParentState="true" :focusable="false"/>
  </qt-view>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "video-item",
  setup() {
    return {

    }
  }
})
</script>

<style scoped>
.video_item{
  width: 392px;
  height: 392px;
  background-color: rgba(0,0,0,0.8);
  flex-direction: column;
  /* focus-background-color:#fff; */
  border-radius: 8px;
}
.video_item_img{
  width: 392px;
  height: 220px;
  background-color: rgba(255,255,255,0.2);
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}
.video_item_img_placeholder{
  width: 392px;
  height: 220px;
  background-color: rgba(255,255,255,0.2);
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}
.video_item_title{
  width: 352px;
  height: 100px;
  font-size: 34px;
  margin-left: 20px;
  margin-right: 20px;
  margin-top: 20px;
  color: #FFFFFF;
}
.video_item_subtitle{
  width: 352px;
  height: 80px;
  font-size: 20px;
  color: #848484;
  margin-left: 20px;
  margin-right: 20px;
}
</style>
