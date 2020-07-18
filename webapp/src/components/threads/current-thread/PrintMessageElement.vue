<template>
    <div class="wrapper">
        <el-upload
                ref="upload"
                class="upload-element"
                action=""
                :on-change="handleChange"
                multiple
                :limit="3"
                :auto-upload="false"
                :on-exceed="handleExceed">
            <i class="el-icon-upload"/>
        </el-upload>
        <el-input
                class="input-large"
                style="{height: max-content; min-height: 100px}"
                type="textarea"
                resize="none"
                autosize
                autofocus
                placeholder="Ctrl + Enter для отправки сообщения"
                @keyup.ctrl.enter.native="handleSubmit"
                v-model="text">
        </el-input>

        <el-input
                class="input-mobile"
                style="{height: 100px; min-height: 100px}"
                type="textarea"
                resize="none"
                autofocus
                placeholder="Печатайте..."
                @keyup.ctrl.enter.native="handleSubmit"
                v-model="text">
        </el-input>

        <el-button @click="handleSubmit" type="primary" icon="el-icon-s-promotion"></el-button>
    </div>
</template>

<script lang="ts">
import {Component, Ref, Vue} from "vue-property-decorator";
import ThreadsModule from "@/store/modules/threads/ThreadsModule";
import {FileType} from "@/common/types/types";
import {ElUpload} from "element-ui/types/upload";

@Component
export default class PrintMessageTextarea extends Vue{
    private text: string = ''
    private uploadedFiles: FileType[] = [];

    @Ref('upload')
    private upload!: ElUpload;

    private handleSubmit() {
        if (this.text.length < 1 && this.uploadedFiles.length < 1) {
            return
        }

        ThreadsModule.sendMessage({
            content: this.text,
            files: this.uploadedFiles
        });
        this.text = '';
        this.uploadedFiles = [];
        this.upload.clearFiles();
        this.$emit('message-sent')
    }

    mounted() {
        setTimeout(() => {
            document.querySelector("textarea")!.style.height = '100%';
        }, 300)
    }

    handleChange(file: FileType, fileList: FileType[]) {
        this.uploadedFiles = fileList ? fileList : [];
    }

    handleExceed(files: FileType[], fileList: FileType[]) {
        this.$message.warning(`The limit is 3, you selected ${files.length} files this time, add up to ${files.length + fileList.length} totally`);
    }
}
</script>
<style scoped lang="less">
    /deep/.el-upload-list--text {
        position: absolute !important;
        right: 0 !important;
        bottom: 150px !important;
    }
    .el-icon-upload {
        margin-top: 13px;
        font-size: 2em;
    }
    .input-large {
        display: flex;
    }

    .input-mobile {
        display: none;
    }
    .wrapper {
        display: flex;
        .upload-element {
            margin-right: 10px;
        }
        button {
            margin-left: 10px;
        }
    }

    @media screen and (max-width: 550px) {
        /deep/.el-upload-list__item {
            max-width: 130px !important;
        }
        .input-large {
            display: none !important;
        }

        .input-mobile {
            display: flex !important;
        }
    }
</style>