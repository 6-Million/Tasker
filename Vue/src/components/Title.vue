<template>
  <div style="height: 110px; background-color: whitesmoke">
    <div style="padding-top: 30px; margin-left: 30px">
      <span style="font-size: 20px; width: 380px; display: inline-block">项目名称：{{message.title}}</span>
      <el-divider direction="vertical"></el-divider>
      <span style="width: 700px; display: inline-block">项目简介：{{message.intro}}</span>
      <el-divider direction="vertical" style="color: black"></el-divider>
      <span>关联团队：group</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "Title",
  props: ['message']
}
</script>

<style scoped>
    .el-divider--vertical{
      display:inline-block;
      width:1px;
      height:3em;
      margin:0 8px;
      vertical-align:middle;
      position:relative;
    }
</style>