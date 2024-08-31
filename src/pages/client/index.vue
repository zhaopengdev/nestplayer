<template>
  <qt-view  style="width: 1920px;height: 1080px;">
    <p class="client_page_label">添加设备</p>
    <qt-column class="client_content" :autofocus="true">
        <qt-view class="client_item_root" :focusable="false" >
          <p class="client_item_text_input" :focusable="false" :duplicateParentState="true">协议：{{protocol}}</p>
        </qt-view>
        <qt-view class="client_item_root" style="margin-top: 24px" :focusable="false" >
<!--          <p class="client_item_text_input" :duplicateParentState="true">{{host}}</p>-->
          <input class="client_item_text_input" :defaultValue="host" v-model="host" :focusable="true"  />
        </qt-view>
        <qt-view class="client_item_root" style="margin-top: 24px" :focusable="false">
          <input class="client_item_text_input" :defaultValue="username" v-model="username" :focusable="true"  />
        </qt-view>
        <qt-view class="client_item_root" style="margin-top: 24px" :focusable="false">
          <input class="client_item_text_input" :defaultValue="password" v-model="password" :focusable="true"  />
        </qt-view>
        <qt-button style="align-self: center;align-items: center;margin-top: 24px" size="medium" text="确认" @click="onClick"/>
      </qt-column>
<!--    <full-key-board ></full-key-board>-->
  </qt-view>
</template>

<script lang="ts">
import {defineComponent} from '@vue/runtime-core'
import {connectSMBServer, getLocalIP} from '../../utils/client'
import FullKeyBoard from "../../components/key-board.vue";
import {useESLog, useESToast} from "@extscreen/es3-core";
import {useESRouter} from "@extscreen/es3-router";

export default defineComponent({
  name: "index.vue",
  components: {FullKeyBoard},
  setup(props, ctx) {
    //

    const protocol = 'smb'
    // let host :string = '192.168.1.'
    // const username = ''
    // const password = ''
    let host :string = '192.168.3.17'
    const username = '15600278700'
    const password = '0511'
    let ESLog = useESLog()
    let TAG = "DebugNestPlayer"
    const router = useESRouter()
    let Toast = useESToast();

    function onESCreate(app,params){
      console.log("onESCreate client")
      getLocalIP().then((res:string) => {
        console.log(`getLocalIP : ${res}`)
        //host = res
      })
    }
    function onClick(e){
      console.log("onClick")
      // connectSMBServer().then((res) => {
      //   console.log(res)
      // })
      connectSMBServer(host, username, password).then((res) => {
        console.log(`connectSMBServer : ${res}`)
        ESLog.i(TAG,`connectSMBServer : ${res}`)
        if(res){
          if(res.code == 200){
            router.push({
              name: 'home',
              params: {}
            });
          }else{
            Toast.showLongToast(`添加错误: ${res.message}`)
          }
        }else{
          Toast.showLongToast(`添加错误: ${res}`)
        }
      }).catch((err) => {
        console.log(`connectSMBServer : ${err}`)
        Toast.showLongToast(`添加错误: ${err}`)
      })
    }
    return {
      onESCreate,
      protocol,
      host,
      username,
      password,
      onClick,
    }
  },
})
</script>


<style scoped>
.client_keyboard{
  width: 752px;
  margin-top: 164px;
  height: 752px;
  align-content: center;
  align-self: center;
  background-color: transparent;
}
.client_content{
  top: 164px;
  height: 752px;
  align-content: center;
  align-self: center;
  background-color: transparent;
}

.client_content_row{
  top: 164px;
  left: 116px;
  height: 752px;
  position: absolute;
  align-content: center;
  align-self: center;
  background-color: transparent;
}

.client_item_root{
  width: 752px;
  focus-scale:1;
  height: 96px;
  enableFocusBorder: true;
  border-radius: 8px;
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
