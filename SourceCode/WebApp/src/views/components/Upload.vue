<template>
  <div class="con-upload">
    <view-upload v-if="viewActive" :src="viewSrc"></view-upload>
    <label v-if="label" class="vs-input--label" for>{{ label }}</label>
    <div class="con-img-upload">
      <div
        v-for="(img,index) in getFilesFilter"
        :class="{
            'fileError':img.error,
            'removeItem':itemRemove.includes(index)
          }"
        :key="index"
        class="img-upload"
      >
        <button class="btn-x-file" @click="removeFile(index)">
          <i translate="no" class="material-icons notranslate">clear</i>
        </button>
        <button
          :class="{
              'on-progress':img.percent,
              'hidden':img.percent >= 100
            }"
          :style="{
              height: `100%`
            }"
          class="btn-upload-file"
          @click="upload(index)"
        >
          <i
            translate="no"
            class="material-icons notranslate"
          >{{ img.percent >= 100?img.error?'report_problem':'cloud_done':'cloud_upload' }}</i>
          <span>{{ img.percent }} %</span>
        </button>
        <img
          v-if="img.src"
          :style="{
              maxWidth:img.orientation == 'h'?'100%':'none',
              maxHeight:img.orientation == 'w'?'100%':'none'
            }"
          :key="index"
          :src="img.src"
          @touchend="viewImage(img.src,$event)"
          @click="viewImage(img.src,$event)"
        >
        <h4 v-if="!img.src" class="text-archive">
          <i translate="no" class="material-icons notranslate">description</i>
          <span>{{ img.name }}</span>
        </h4>
      </div>

      <div
        :class="{
          'on-progress-all-upload':percent != 0,
          'is-ready-all-upload':percent >= 100,
          'disabled-upload':$attrs.hasOwnProperty('disabled') || limit?(srcs.length - itemRemove.length) >= Number(limit):false
        }"
        class="con-input-upload"
      >
        <input
          ref="fileInput"
          v-bind="$attrs"
          :disabled="$attrs.disabled || limit?(srcs.length - itemRemove.length) >= Number(limit):false"
          :data-vv-as="label"
          type="file"
          v-validate="rules"
          @change="getFiles"
        >
        <span class="text-input">{{ text }}</span>
        <span :style="{
            width:`${percent}%`
          }" class="input-progress"></span>
      </div>
    </div>
  </div>
</template>
<script>
import ViewUpload from "./ViewUpload.vue";
import auth from "@auth";
import $http from "@http";

var lastTap = 0;
export default {
  name: "CrUpload",
  inject: ["$validator"],
  components: {
    ViewUpload
  },
  inheritAttrs: false,
  props: {
    label: {
      default: null,
      type: String
    },
    files: {
      default: null,
      type: Array
    },
    fileName: {
      default: null,
      type: String
    },
    text: {
      default: "Upload File",
      type: String
    },
    textMax: {
      default: "Maximum of files reached",
      type: String
    },
    limit: {
      default: null,
      type: [Number, String]
    },
    action: {
      default: null,
      type: String
    },
    headers: {
      default: null,
      type: Object
    },
    data: {
      default: null,
      type: Object
    },
    automatic: {
      default: false,
      type: Boolean
    },
    showUploadButton: {
      default: true,
      type: Boolean
    },
    singleUpload: {
      default: false,
      type: Boolean
    },
    rules: {
      default: null,
      type: String
    }
  },
  data: () => ({
    inputValue: null,
    type: null,
    srcs: [],
    filesx: [],
    itemRemove: [],
    percent: 0,
    viewActive: false,
    viewSrc: null
  }),
  mounted() {
    if (this.files) {
      this.addFiles(this.files);
    }
  },
  computed: {
    getFilesFilter() {
      let files = this.srcs.filter(item => {
        return !item.remove;
      });

      return files;
    },
    postFiles() {
      let postFiles = Array.prototype.slice.call(this.filesx);
      postFiles = postFiles.filter(item => {
        return !item.hasOwnProperty("remove");
      });
      return postFiles.length;
    }
  },
  watch: {
    percent() {
      if (this.percent >= 100) {
        this.srcs.forEach(file => {
          file.percent = 100;
        });
        setTimeout(() => {
          this.percent = 0;
        }, 1000);
      }
    }
  },
  methods: {
    viewImage(src, evt) {
      var timeout;

      var eventx =
        "ontouchstart" in window ||
        (window.DocumentTouch && document instanceof window.DocumentTouch)
          ? "touchstart"
          : "click";
      if (eventx === "click") {
        this.viewActive = true;
        this.viewSrc = src;
      } else {
        if (evt.type === "touchend") {
          var currentTime = new Date().getTime();
          var tapLength = currentTime - lastTap;
          clearTimeout(timeout);
          if (tapLength < 500 && tapLength > 0) {
            this.viewActive = true;
            this.viewSrc = src;
            event.preventDefault();
          }
          lastTap = currentTime;
        }
      }
    },
    removeFile(index) {
      this.itemRemove.push(index);
      this.$emit("on-delete", this.filesx[index]);
      setTimeout(() => {
        this.filesx[index].remove = true;
      }, 301);
    },
    async addFiles(files) {
      let _this = this;

      function uploadImage(src) {
        let orientation = "h";
        let image = new Image();
        image.src = src;
        image.onload = function() {
          if (this.width > this.height) {
            orientation = "w";
          }
          switchImage(this, orientation);
        };
        image.onerror = function() {
          _this.srcs.push({
            src: image.src,
            orientation: orientation,
            type: _this.typex,
            percent: 100,
            error: true,
            remove: null
          });
        };
      }

      function switchImage(image, orientation) {
        _this.srcs.push({
          src: image.src,
          orientation: orientation,
          type: _this.typex,
          percent: 100,
          error: false,
          remove: null
        });
      }

      for (const index in files) {
        if (files.hasOwnProperty(index)) {
          const file = files[index];
          let filex = null;
          if (file.type === "image") {
            let response = await fetch(file.src);
            let data = await response.blob();
            filex = new File([data], file.name);
          } else {
          }

          if (file.type === "image") {
            this.typex = "image";
            this.filesx.push(filex);
            uploadImage(file.src);
          } else if (file.type === "video") {
            this.typex = "video";
            this.filesx.push(filex);
            _this.srcs.push({
              src: null,
              name: file.name,
              type: "video",
              percent: 100,
              error: false,
              remove: null
            });
          } else {
            this.filesx.push(filex);
            _this.srcs.push({
              src: null,
              name: file.name,
              percent: 100,
              error: false,
              remove: null
            });
          }
          this.$emit("change", file.src, this.filesx);
        }
      }
      const input = this.$refs.fileInput;
      input.type = "text";
      input.type = "file";
    },
    getFiles(e) {
      this.$emit("update:vsFile", e.target.value);
      let _this = this;
      function uploadImage(e) {
        let orientation = "h";
        var image = new Image();
        image.src = e.target.result;
        image.onload = function() {
          if (this.width > this.height) {
            orientation = "w";
          }
          switchImage(this, orientation);
        };
      }
      function switchImage(image, orientation) {
        _this.srcs.push({
          src: image.src,
          orientation: orientation,
          type: _this.typex,
          percent: null,
          error: false,
          remove: null
        });
      }

      var files = e.target.files;
      let count = this.srcs.length - this.itemRemove.length;
      for (const file in files) {
        if (files.hasOwnProperty(file)) {
          if (this.limit) {
            count++;
            if (count > Number(this.limit)) {
              break;
            }
          }

          var reader = new FileReader();
          const filex = files[file];
          if (/image.*/.test(filex.type)) {
            this.typex = "image";
            this.filesx.push(filex);
            reader.onload = uploadImage;
            reader.readAsDataURL(filex);
          } else if (/video.*/.test(filex.type)) {
            this.typex = "video";
            this.filesx.push(filex);
            _this.srcs.push({
              src: null,
              name: filex.name,
              type: "video",
              percent: null,
              error: false,
              remove: null
            });
          } else {
            this.filesx.push(filex);
            _this.srcs.push({
              src: null,
              name: filex.name,
              percent: null,
              error: false,
              remove: null
            });
          }
          this.$emit("change", e.target.value, this.filesx);
        }
      }

      if (this.automatic) {
        this.upload(this.filesx.length - 1);
      }
    },
    upload(index) {
      const formData = new FormData();
      let postFiles = Array.prototype.slice.call(this.filesx);
      if (typeof index === "number") {
        postFiles = [postFiles[index]];
      } else if (index === "all") {
        postFiles = postFiles.filter(item => {
          return !item.hasOwnProperty("remove");
        });
      }

      const data = this.data || {};
      for (var key in data) {
        formData.append(key, data[key]);
      }

      if (this.singleUpload) {
        postFiles.forEach(filex => {
          const formData = new FormData();
          for (var key in data) {
            formData.append(key, data[key]);
          }
          formData.append(this.fileName, filex, filex.name);

          this.uploadx(index, formData);
        });
      } else {
        postFiles.forEach(filex => {
          formData.append(this.fileName, filex, filex.name);
        });
        this.uploadx(index, formData);
      }
    },
    uploadx(index, formData) {
      let self = this;
      const xhr = new XMLHttpRequest();

      xhr.onerror = function error(e) {
        self.$emit("on-error", e);
        if (typeof index === "number") {
          self.srcs[index].error = true;
        }
      };

      xhr.onload = function onload(e) {
        if (xhr.status < 200 || xhr.status >= 300) {
          self.$emit("on-error", e);
          if (typeof index === "number") {
            self.srcs[index].error = true;
          }
        } else {
          let response = JSON.parse(e.target.responseText);
          if (response.status === 1) {
            self.$emit("on-success", response.data);
          } else {
            self.$emit("on-error", e);
            if (typeof index === "number") {
              self.srcs[index].error = true;
            }
          }
        }
      };

      if (xhr.upload) {
        xhr.upload.onprogress = function progress(e) {
          if (e.total > 0) {
            let percent = (e.loaded / e.total) * 100;
            if (typeof index === "number") {
              self.srcs[index].percent = Math.trunc(percent);
            } else {
              self.percent = Math.trunc(percent);
            }
          }
        };
      }

      xhr.withCredentials = true;
      let action = this.action;
      if (action === null) {
        // HardCode
        action = `${$http.baseUrl}/transaction/upload`;
      }
      xhr.open("POST", action);

      const headers = this.headers || {};
      const accessToken = auth.getAccessToken();
      if (accessToken !== null && !auth.tokenIsExpired()) {
        headers.Authorization = `Bearer ${accessToken}`;
      }
      for (let head in headers) {
        if (headers.hasOwnProperty(head) && headers[head] !== null) {
          xhr.setRequestHeader(head, headers[head]);
        }
      }

      xhr.send(formData);
    }
  }
};
</script>

<style lang="scss" scoped>
.disabled-upload {
  display: none;
}
</style>
