<template>
  <qt-view  style="width: 1920px;height: 1080px;">
    <p class="client_page_label">添加设备</p>
    <qt-column class="client_content" :autofocus="true">
      <qt-view class="client_item_root" :focusable="true" >
        <p class="client_item_text_input" :duplicateParentState="true">协议：{{protocol}}</p>
      </qt-view>
      <qt-view class="client_item_root" style="margin-top: 24px" :focusable="true" >
        <p class="client_item_text_input" :duplicateParentState="true">{{host}}</p>
      </qt-view>
      <qt-view class="client_item_root" style="margin-top: 24px" :focusable="true">
        <p class="client_item_text_input" :duplicateParentState="true">{{username}}</p>
      </qt-view>
      <qt-view class="client_item_root" style="margin-top: 24px" :focusable="true">
        <p class="client_item_text_input" :duplicateParentState="true">{{password}}</p>
      </qt-view>

    </qt-column>
  </qt-view>
</template>

<script lang="ts">
import {defineComponent} from '@vue/runtime-core'


export default defineComponent({
  name: "index.vue",
  setup(props, ctx) {
    const protocol = 'smb'
    const host = '192.168.1.'
    const username = 'zhaopeng'
    const password = '*****'

    function onESCreate(app,params){
      console.log("onESCreate client")
    }
    return {
      onESCreate,
      protocol,
      host,
      username,
      password
    }
  },
})
</script>


<style scoped>
.client_content{
  width: 752px;
  margin-top: 164px;
  height: 752px;
  align-content: center;
  align-self: center;
  background-color: transparent;
}

.client_item_root{
  width: 752px;
  focus-scale:1;
  height: 96px;
  justify-content: center;
  background-color: #1D201F;
  focus-background-color: #E1E3E0;
}

.client_page_label{
  width: 500px;
  font-size: 48px;
  color: #C4C7C4;
  top: 128px;
  left: 116px;
}

.client_item_text_input{
  width: 752px;
  height: 40px;
  font-size: 36px;
  left: 32px;
  background-color: transparent;
  color: #C4C7C4;
  focus-color: #191C1B;
}

</style>
