import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Login from "@/views/Login.vue";
import App from "@/App.vue";
import Register from "@/views/Register.vue";
import Project from "@/views/Project.vue";
import ProjList from "@/views/ProjList.vue";
import Task from "@/views/Task.vue";
import TaskList from "@/views/TaskList.vue";
import Group from "@/views/Group.vue";
import GroupList from "@/views/GroupList.vue";
import FileList from "@/views/FileList.vue";
import Comment from "@/views/Comment.vue";
import Chat from "@/views/Chat.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'app',
    component: App,
    redirect:'/home',
    children:[
      {
        // 主页面
        path: '/home',
        name: 'home',
        component: HomeView
      }
    ]
  },
  {
    // 登录页面
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    // 注册页面
    path: '/register',
    name: 'register',
    component: Register
  },
  {
    // 项目页面
    path: '/project',
    name: 'project',
    component: Project
  },
  {
    // 项目列表
    path: '/projlist',
    name: 'projlist',
    component: ProjList
  },
  {
    // 任务页面
    path: '/task',
    name: 'task',
    component: Task
  },
  {
    // 任务列表
    path: '/tasklist',
    name: 'tasklist',
    component: TaskList
  },
  {
    // 团队页面
    path: '/group',
    name: 'group',
    component: Group
  },
  {
    // 团队列表
    path: '/grouplist',
    name: 'grouplist',
    component: GroupList
  },
  {
    // 文件列表
    path: '/filelist',
    name: 'filelist',
    component: FileList
  },
  {
    // 审批列表
    path: '/comment',
    name: 'comment',
    component: Comment
  },
  {
    // 聊天
    path: '/chat',
    name: 'chat',
    component: Chat
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
