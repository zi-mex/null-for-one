<template>
  <div>
    <div class="nav" style="border: 1px solid #ccc; margin-top: 10px;color: #000">
      <!-- 工具栏 -->
      <Toolbar
          style="border-bottom: 1px solid #ccc"
          :editor="myEditor"
          :defaultConfig="toolbarConfig"
      />
      <!-- 编辑器 -->
      <Editor
          style="height: 400px; overflow-y: hidden"
          :defaultConfig="editorConfig"
          v-model="html"
          @onChange="onChange"
          @onCreated="onCreated"
      />
    </div>
  </div>
</template>
<script setup>
import {ref, defineProps, onMounted, onBeforeUnmount} from 'vue';
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import utils from "@/utils/tools.js";

const props = defineProps({
  content: {
    type: String,
    default: "",
  }
});

const myEditor = ref(null);
const html = ref(props.content);
const toolbarConfig = ref({
  // toolbarKeys: [ /* 显示哪些菜单，如何排序、分组 */ ],
  // excludeKeys: [ /* 隐藏哪些菜单 */ ],
});

const editorConfig = ref({
  placeholder: "请输入内容...",
  // autoFocus: false,
  MENU_CONF: {
    uploadImage: {
      server: import.meta.env.VITE_APP_API_URL + "/file/upload",
      headers: {
        token: utils.getToken()
      },
      fieldName: 'file',
      customInsert(res, insertFn) {
        insertFn(res.data.url, res.data.name, res.data.url);
      },
    },
    uploadVideo: {
      maxFileSize: 100 * 1024 * 1024, // 10M
      server: import.meta.env.VITE_APP_API_URL + "/file/upload",
      headers: {
        token: utils.getToken()
      },
      fieldName: 'file',
      customInsert(res, insertFn) {
        insertFn(res.data.url, res.data.name, res.data.url);
      },
    }
  }
});

function onCreated(editor) {
  myEditor.value = Object.seal(editor);
}

const emit = defineEmits(['content-change']);

function onChange(editor) {
  html.value = editor.getHtml();
  // 由于我们使用的是Vue 3的Composition API，所以不需要使用this.$emit
  emit("content-change", html.value);
}

onMounted(() => {
  // 如果需要在组件挂载后执行某些操作，可以在这里添加
});

onBeforeUnmount(() => {
  if (myEditor.value != null) {
    myEditor.value.destroy();
  }
});
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
<style>
.nav .title {
  margin-top: 10px;
  color: #000 !important;
}
</style>
