<template>
  <qt-view class="video_item">

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
  background-color: darkcyan;
  /* focus-background-color:#fff; */
  border-radius: 8px;
}

</style>
