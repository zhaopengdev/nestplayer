<template>
  <qt-grid-view class="full_keyboard_list" ref="keyboard_grid_view" name="grid_view"
                :clipChildren="false" :autofocusPosition="14"
                 :spanCount="6">
    <qt-view :type="1" :focusable="true" :focusScale="1.1"
             class="full_keyboard_item"
             eventClick
             eventFocus
             :clipChildren="false">
      <div class="full_keyboard_item_focus"
           :gradientBackground="{ orientation: 6, colors: ['#FF0057FF', '#FF00C7FF'], cornerRadius: 8 }"
           showOnState="focused" :duplicateParentState="true"></div>
      <qt-text :duplicateParentState="true" :ellipsizeMode="2" gravity="center" :fontSize="40" :focusable="false"
               class="full_keyboard_item_text" text="${text}" />
    </qt-view>
  </qt-grid-view>
</template>

<script lang="ts">
import {defineComponent, nextTick, onMounted, ref} from 'vue'
import {QTGridViewItem, QTIGridView} from "@quicktvui/quicktvui3";

export default defineComponent({
  name: "full-key-board",
  setup(props, ctx) {
    const keyboard_grid_view = ref<QTIGridView>()
    let keyboardItems: Array<QTGridViewItem> = [
      { text: "A", type: 1 }, { text: "B", type: 1 }, { text: "C", type: 1 }, { text: "D", type: 1 },
      { text: "E", type: 1 }, { text: "F", type: 1 }, { text: "G", type: 1 }, { text: "H", type: 1 },
      { text: "I", type: 1 }, { text: "J", type: 1 }, { text: "K", type: 1 }, { text: "L", type: 1 },
      { text: "M", type: 1 }, { text: "N", type: 1 }, { text: "O", type: 1 }, { text: "P", type: 1 },
      { text: "Q", type: 1 }, { text: "R", type: 1 }, { text: "S", type: 1 }, { text: "T", type: 1 },
      { text: "U", type: 1 }, { text: "V", type: 1 }, { text: "W", type: 1 }, { text: "X", type: 1 },
      { text: "Y", type: 1 }, { text: "Z", type: 1 }, { text: "1", type: 1 }, { text: "2", type: 1 },
      { text: "3", type: 1 }, { text: "4", type: 1 }, { text: "5", type: 1 }, { text: "6", type: 1 },
      { text: "7", type: 1 }, { text: "8", type: 1 }, { text: "9", type: 1 }, { text: "0", type: 1 }
    ]
    onMounted(() => {
      //console.log("full-key-board onMounted")
      nextTick(() => {
        console.log("full-key-board nextTick")
        keyboard_grid_view.value?.init(keyboardItems)
      })
    })
    return {
      keyboard_grid_view,
      keyboardItems
    }
  },
})
</script>



<style scoped>
.full_keyboard_list {
  width: 480px;
  height: 480px;
  background-color: blue;
}
.full_keyboard_item{
  width: 80px;
  height: 80px;
  background-color: transparent;
  /* focus-background-color:#fff; */
  border-radius: 8px;
}
.full_keyboard_item_focus {
  width: 80px;
  height: 80px;
  background-color: transparent;
  border-radius: 8px;
  position: absolute;
}

.full_keyboard_item_text{
  width: 80px;
  height: 80px;
  color: #fff;
  /* focus-color: rgba(0, 0, 0, 1); */
  border-radius: 8px;
}
</style>
