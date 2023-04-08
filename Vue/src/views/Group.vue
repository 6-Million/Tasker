<template>
  <div>
    <Header/>
    <div style="height: 10px"></div>
    <div style="float: left; height: 627px; width: 450px; margin-left: 10px; background-color: floralwhite">
      <div style="margin-left: 30px; margin-top: 50px; font-size: large">
        团队名称：{{groupData.groupName}}
        <br/>
        <br/>
        <br/>
        <br/>
        团队ID：{{groupData.gid}}
        <br/>
        <br/>
        <br/>
        <br/>
        角色：
        <span v-if="groupData.resp === 1">团队负责人</span>
        <span v-if="groupData.resp === 0">团队成员</span>
        <br/>
        <br/>
        <br/>
        <br/>
        关联项目：<a class="link" @click="gotoProj">{{projectData.title}}</a>
        <br/>
        <br/>
        <br/>
        <br/>
        <div v-if="groupData.resp===1">负责人操作：
          <el-button @click="addMember" size="medium" type="success" style="margin-left: 10px;">添加成员</el-button>
          <br/>
          <br/>
          <br/>
        </div>
        团队聊天：<a class="link" @click="gotoChat()">加入群聊</a>
      </div>
    </div>
    <!--  团队成员展示部分  -->
    <div style="float: right; height: 627px; width: 1050px; margin-right: 10px; background-color: white">
      <div style="padding-top: 10px; color: #606266; font-size: 23px; text-align: center">团队成员</div>
      <!--      表格展示-->
      <div style="padding: 20px">
        <el-table
            class="list"
            :data="tableData"
            border
            stripe
            style="width: 100%">
          <el-table-column
              prop="uid"
              label="用户UID"
              width="250">
          </el-table-column>
          <el-table-column
              prop="name"
              label="姓名"
              width="250">
          </el-table-column>
          <el-table-column
              prop="company"
              label="所属公司"
              width="250">
          </el-table-column>
          <el-table-column
              prop="resp"
              label="角色"
              sortable>
          </el-table-column>
        </el-table>
        <!--        分页-->
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            layout="prev, pager, next"
            :total="100"
            style="margin-top: 20px; margin-left: 310px; float: left">
        </el-pagination>
      </div>
      <!--      新增弹窗-->
      <el-dialog
          title="添加成员"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">
        <el-form ref="form" :model="form" status-icon :rules="rules" label-width="120px">
          <el-form-item label="用户UID" prop="uid">
            <el-input v-model="form.uid" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save('form')">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import request from "@/utils/request";

export default {
  name: "Group",
  components: {Footer, Header},
  data(){
    return{
      user: '',
      groupKey: '',
      groupData: [],
      projectData: [],
      currentPage: 1,
      tableData: [],
      dialogVisible: false,
      form:{
        uid: '',
        resp: '',
        gid: '',
        pid: '',
        groupName: '',
      },
      rules: {
        uid: [{required: true, message: '请输入用户UID', trigger: 'blur'}]
      }
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check();
    this.getParams();
    this.load();
  },
  methods: {
    check(){
      if(!this.user)
        this.$router.push('/login')
    },
    getParams(){
      this.groupKey = this.$route.query.groupKey;
    },
    load(){
      request.get("/group/bykey",{params:{key: this.groupKey}}).then(res => {
        this.groupData = res.data;
        request.get("/project/byid",{params:{pid: this.groupData.pid}}).then(res => {
          this.projectData = res.data;
          request.get("/group/bygid",{params:{pageNum: this.currentPage, pageSize:9, gid:this.groupData.gid}}).then(res => {
            this.tableData = res.data.records;
            this.tableData.filter((item) => {
              if(item.resp === 1)
                item.resp = "团队负责人"
              if(item.resp === 0)
                item.resp = "团队成员"
              request.get("user/byid", {params:{uid: item.uid}}).then(res => {
                this.$set(item,"name",res.data.name)
                this.$set(item,"company",res.data.company)
              })
            })
          })
        })
      })
    },
    gotoProj(){
      this.$router.push({
        path:'/project',
        query:{
          projectId: this.projectData.pid
        }
      })
    },
    gotoChat(){
      this.$router.push({
        path:'/chat',
        query:{
          groupKey: this.groupKey
        }
      })
    },
    handleCurrentChange(val){
      this.currentPage = val;
      this.load()
    },
    addMember(){
      this.dialogVisible = true;
      this.form = {};
    },
    save(formName){
      this.$refs[formName].validate((valid) => {
        if(valid){
          this.form.pid = this.groupData.pid
          this.form.resp = 0
          this.form.gid = this.groupData.gid
          this.form.groupName = this.groupData.groupName
          request.post("/group/add",this.form).then(res => {
            if(res.code === '0'){
              this.$message({
                type: "success",
                message: "添加成功！"
              })
              this.load()
              this.dialogVisible = false
            }else{
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }else{
          alert("请检查！！");
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.link{
  color: limegreen;
  text-decoration: underline;
}
.link:hover{
  cursor: pointer;
}
</style>