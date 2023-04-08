<template>
  <div>
    <Header/>
    <div style="height: 10px"></div>
<!--    <Title v-bind:message="projectData"/>-->
    <!--  項目介紹部分  -->
    <div style="float: left; height: 627px; width: 450px; margin-left: 10px; background-color: floralwhite">
      <div style="margin-left: 30px; margin-top: 50px; font-size: large">
        项目名称：{{projectData.title}}
        <br/>
        <br/>
        <br/>
        项目简介：{{projectData.intro}}
        <br/>
        <br/>
        <br/>
        创建时间：{{projectData.startdate}}
        <br/>
        <br/>
        <br/>
        截止时间：{{projectData.ddl}}
        <br/>
        <br/>
        <br/>
        状态：{{projectData.done}}
        <br/>
        <br/>
        <br/>
        关联团队：<a class="link" @click="gotoGroup">{{groupData.groupName}}</a>
        <br/>
        <br/>
        <br/>
        <div v-if="groupData.resp===1">负责人操作：
          <el-button @click="doneProj" size="medium" type="success" style="margin-left: 10px;">完成项目</el-button>
          <el-button @click="endProj" size="medium" type="success" style="margin-left: 20px;">终止项目</el-button>
        </div>
      </div>
    </div>
    <!--  关联任务展示部分  -->
    <div style="float: right; height: 627px; width: 1050px; margin-right: 10px; background-color: white">
      <div style="padding-top: 10px; color: #606266; font-size: 23px; text-align: center">关联任务</div>
      <!--      表格展示-->
      <div style="padding: 20px">
        <el-table
            class="list"
            :data="tableData"
            border
            stripe
            style="width: 100%"
            @row-click="openDetails">
          <el-table-column
              prop="title"
              label="任务名称"
              width="220">
          </el-table-column>
          <el-table-column
              prop="uid"
              label="对接人"
              width="220">
          </el-table-column>
          <el-table-column
              prop="ddl"
              label="截止时间"
              sortable
              width="220">
          </el-table-column>
          <el-table-column
              prop="done"
              label="状态"
              width="180"
              :filters="[{ text: '已完成', value: '已完成' }, { text: '未完成', value: '未完成' },{ text: '待审核', value: '待审核' }, { text: '已终止', value: '已终止' }]"
              :filter-method="filterDone">
          </el-table-column>
          <el-table-column
              prop="emer"
              label="重要程度"
              :filters="[{ text: '非常重要', value: '非常重要' }, { text: '重要', value: '重要' }, { text: '普通', value: '普通' }, { text: '不重要', value: '不重要' }]"
              :filter-method="filterEmer">
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
        <div style="float: right; margin-top: 15px">
          <el-button @click="add" size="medium" type="success" style="margin-right: 10px;">新增分配任务</el-button>
        </div>
      </div>
      <!--      新增弹窗-->
      <el-dialog
          title="新增任务"
          :visible.sync="dialogVisible"
          width="30%">
        <el-form ref="form" :model="form" status-icon :rules="rules" label-width="120px">
          <el-form-item label="任务名称" prop="title">
            <el-input v-model="form.title" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="任务简介" prop="intro">
            <el-input v-model="form.intro" type="textarea" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="对接人UID" prop="uid">
            <el-input v-model="form.uid" type="textarea" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="startdate">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.startdate" style="width: 80%;"></el-date-picker>
          </el-form-item>
          <el-form-item label="截止时间" prop="ddl">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.ddl" style="width: 80%;"></el-date-picker>
          </el-form-item>
          <el-form-item label="重要程度" prop="emer">
            <el-radio-group v-model="form.emer">
              <el-radio label="非常重要"></el-radio>
              <el-radio label="重要"></el-radio>
              <el-radio label="普通"></el-radio>
              <el-radio label="不重要"></el-radio>
            </el-radio-group>
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
import Title from "@/components/Title.vue";
import request from "@/utils/request";


export default {
  name: "Project",
  components: {Title, Footer, Header},
  data(){
    return{
      user: "",
      projectId: '',
      projectData: [],
      groupData: [],
      tableData: [],
      currentPage: 1,
      dialogVisible: false,
      form: {
        title: '',
        intro: '',
        uid: '',
        startdate: '',
        ddl: '',
        pid: '',
        coop: '',
        emer: ''
      },
      rules: {
        title: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          {max:10, message: '不能超过10位', trigger: 'blur' }
        ],
        intro: [
          {max:30, message: '不能超过30位', trigger: 'blur' }
        ],
        uid: [
          {required: true, message: '请输入对接人UID', trigger: 'blur'}
        ],
        startdate: [
          {required: true, message: '请选择日期', trigger: 'blur'}
        ],
        ddl: [
          {required: true, message: '请选择日期', trigger: 'blur'}
        ],
        emer: [
          {required: true, message: '请选择重要程度', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check();
    this.getParams();
    this.getProj();
    this.getGroup();
    this.load();
  },
  methods: {
    check(){
      if(!this.user)
        this.$router.push('/login')
    },
    getParams(){
      this.projectId = this.$route.query.projectId;
    },
    getProj(){
      request.get("/project/byid", {params:{pid: this.projectId}}).then(res => {
        this.projectData = res.data;
        if(this.projectData.done === 0)
          this.projectData.done = "未完成"
        if(this.projectData.done === 1)
          this.projectData.done = "已完成"
        if(this.projectData.done === 2)
          this.projectData.done = "已终止"
      })
    },
    getGroup(){
      request.get("/group/bypid",{params:{pid: this.projectId, uid: this.user.uid}}).then(res => {
        this.groupData = res.data;
      })
    },
    gotoGroup(){
      this.$router.push({
        path:'/group',
        query:{
          groupKey: this.groupData.groupKey
        }
      })
    },
    doneProj(){
      this.projectData.done = 1
      request.put("project/done",this.projectData).then(res => {
            if(res.code === '0') {
              this.$message({
                type: "success",
                message: "操作成功！"
              })
              this.getProj()
              this.load()
            }
      })
    },
    endProj(){
      this.projectData.done = 2
      request.put("project/end",this.projectData).then(res => {
        if(res.code === '0') {
          this.$message({
            type: "success",
            message: "操作成功！"
          })
          this.getProj()
          this.load()
        }
      })
    },
    filterDone(value, row) {
      return row.done === value;
    },
    filterEmer(value, row) {
      return row.emer === value;
    },
    handleCurrentChange(val){
      this.currentPage = val;
      this.load()
    },
    openDetails(row){
      this.$router.push({
        path:'/task',
        query:{
          taskId: row.tid
        }
      })
    },
    add(){
      this.dialogVisible = true;
      this.form = {};
    },
    save(formName){
      this.$refs[formName].validate((valid) => {
        if(valid){
          if(this.form.emer === '非常重要')
            this.form.emer = 0
          if(this.form.emer === '重要')
            this.form.emer = 1
          if(this.form.emer === '普通')
            this.form.emer = 2
          if(this.form.emer === '不重要')
            this.form.emer = 3
          this.form.pid = this.projectId
          this.form.coop = 1
          request.post("/task/addIndp",this.form).then(res => {
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
    },
    load(){
      request.get("/task/loadInProj",
          {params:{pageNum: this.currentPage, pageSize:9, pid: this.projectId}}).then(res => {
        this.tableData = res.data.records;
        this.tableData.filter((item) => {
          if(item.done === 0)
            item.done = "未完成"
          if(item.done === 1)
            item.done = "已完成"
          if(item.done === 2)
            item.done = "待审核"
          if(item.done === 3)
            item.done = "已终止"
          if(item.emer === 0)
            item.emer = "非常重要"
          if(item.emer === 1)
            item.emer = "重要"
          if(item.emer === 2)
            item.emer = "普通"
          if(item.emer === 3)
            item.emer = "不重要"
          request.get("user/byid",{params:{uid: item.uid}}).then(res => {
            item.uid = res.data.name + " (uid: " + item.uid + ")";
          })
        })
      })
    },
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
.list:hover{
  cursor: pointer;
}
</style>