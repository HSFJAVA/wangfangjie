<template>
<div class="app-container">
  <div>
    <el-button type="primary" size="mini" plain @click="onSubmit" icon="el-icon-search">查询</el-button>
    <el-button type="primary" size="mini" plain @click="editYw()">修改</el-button>
    <el-button type="primary" size="mini" plain @click="dialogFormVisibleNew = true">新建模板 </el-button>
    <el-button type="danger" size="mini" plain @click="deleteTab()">删除</el-button>
  </div>
  <el-form :inline="true" :model="formSearch" class="demo-form-inline" style="margin: 20px 0" size="mini">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-form-item label="模板名称"  >
          <el-input  placeholder="模板名称" v-model="formSearch.name" size="mini"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="客户名称">
          <el-select
            v-model="formSearch.custName"
            filterable
            placeholder="请选择"
            style="width:100%"
            size="mini"
          >
            <el-option
              v-for="item in options4"
              :key="item.ecustomid"
              :label="item.customName"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="单证类型">
          <!-- <el-input v-model="formSearch.yewuType" placeholder="单证类型" size="mini"></el-input> -->
          <el-select v-model="formSearch.yewuType" clearable placeholder="请选择" size="mini">
            <el-option
              v-for="item in optionType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
            <el-col :span="6">
        <el-form-item label="创建人">
          <el-input v-model="formSearch.creater" placeholder="创建人" size="mini"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <el-table
    v-loading="loading"
    element-loading-text="拼命加载中"
    height="600px"
    ref="multipleTable"
    :data="tableData3"
    tooltip-effect="dark"
    style="width: 100%"
    size="small"
    @selection-change="handleSelectionChange"
    border
    :highlight-current-row="true"
  >
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column type="index" width="50" fixed></el-table-column>
    <el-table-column label="操作" width="80px" fixed>
      <template slot-scope="scope">
        <el-button type="success" plain size="mini" @click="goEdit(scope.$index, scope.row)">详情</el-button>
      </template>
    </el-table-column>
    <el-table-column prop="remarks1" label="模板名称" width="200px"></el-table-column>
    <el-table-column prop="remarks2" label="客户业务编号" width="200px"></el-table-column>
    <el-table-column prop="remarks4" label="客户名称" width="200px"></el-table-column>
    <el-table-column prop="create_user" label="创建人" width="200px"></el-table-column>
    <el-table-column prop="create_time" label="创建时间" width="180px">
        <template slot-scope="scope">{{moment(scope.row.create_time).format("YYYY-MM-DD HH:mm:ss")}}</template>
     </el-table-column>
    <!-- <el-table-column prop="creat_time" label="创建时间" width="220px"></el-table-column> -->
    <el-table-column prop="update_user" label="更新人" width="200px"></el-table-column>
    <!-- <el-table-column prop="update_time" label="更新时间" width="220px"></el-table-column> -->
     <el-table-column prop="update_time" label="更新" width="180px">
        <template slot-scope="scope">{{moment(scope.row.update_time).format("YYYY-MM-DD HH:mm:ss")}}</template>
     </el-table-column>
  </el-table>
  <el-row class="custrowpage">
    <div class="block pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[20, 40, 80, 100]"
        :page-size="10"
        :current-page="currentPage4"
        layout="total, sizes, prev, pager, next, jumper"
        :total="400"
      ></el-pagination>
    </div>
  </el-row>

  <!-- 新建单证的dialogvisible -->
  <el-dialog title="创建模板" :visible.sync="dialogFormVisibleNew" width="35%">
    <el-form :model="Newyewu" label-width="140px" size="small"> 
      <el-form-item label="客户名称">
        <el-select
          v-model="formInline.custom_key"
          filterable
          placeholder="请选择"
          style="width:100%"
        >
          <el-option
            v-for="item in options4"
            :key="item.ecustomid"
            :label="item.customName"
            :value="item.customName"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="模板名称">
        <el-input v-model="formInline.remarks1" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="单证类型">
        <el-select v-model="formInline.remarks2" placeholder="单证类型" style="width:100%">
          <el-option label="进仓" value="0"></el-option>
          <el-option label="出仓" value="1"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="editcencal()">取 消</el-button>
      <el-button type="primary" @click="submitNewYewu()">确 定</el-button>
    </div>
  </el-dialog>
  <!-- 这是编辑的dialogvislable -->
  <el-dialog title="修改內容" :visible.sync="dialogFormVisibleEdit" center width="30%">
    <el-form :model="formYwEdit" size="mini">
      <el-form-item label="业务编号" :label-width="formLabelWidth">
        <el-input v-model="formYwEdit.customNo" auto-complete="off"></el-input>
      </el-form-item> 
      <el-form-item label="备注" :label-width="formLabelWidth">
        <el-input v-model="formYwEdit.remark" auto-complete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="editcencal">取 消</el-button>
      <el-button type="primary" @click="eEditYw()">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
/* eslint-disable */
import { open } from "fs";
import { getsession } from "../../utils/pramAll";
export default {
name: "importTemplate",
data() {
  return {
    multipleTable: [],
    multipleSelection: [],
    dialogFormVisibleNew: false,
    dialogFormVisibleEdit: false,
    tableData3: [],
    session: "",
    formLabelWidth: "80px",
    formLabelWidth1: "130px",
    loading: false,
    state2: "",
    formYwEdit: {},
    uplodForm:{
      custNo: "",
      custName: "",
      type:"",
      remark:"",
      file: ""

    },
    edittable: false,
    optionType: [
      {
        value: 0,
        label: "进仓"
      },
      {
        value: 1,
        label: "出仓"
      }
    ],
    formInline: {
      custom_key: "",
      remarks1: "",
      remarks2: "",
      creat_user: ""
    },
    Newyewu: {
      custname: ""
    },
    formSearch: {
      name: "",
      onlyNo: "",
      yewuNo: "",
      custName: "",
      creater: "",
      date: "",
      yewuState: "",
      yewuType: "",
      jiedanRemark: ""
    },
    searchcCndetion:{
        page:"1", rows:"20",  type:"",  name:"",  user:"",  custom:""
    },
    rules:{
          customNo: [{
            required: true, message: '请输入合适内容', trigger: 'blur'
          }]
    },
    yewuState:'',
    options4: [],
    value9: [],
    list: [],
    creater:'',
    loading: false,
    states: [],
    status1: "",
    createDate: "",
    custNo: "",
    op_user: "",
    startDate: "",
    endDate: "",
    ywNo: "",
    currentPage4: 1,
    offset: 1,
    limit: 100,
    pickerOptions2: {
      shortcuts: [
        {
          text: "最近一周",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit("pick", [start, end]);
          }
        },
        {
          text: "最近一个月",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit("pick", [start, end]);
          }
        },
        {
          text: "最近三个月",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit("pick", [start, end]);
          }
        }
      ]
    },
    value6: "",
    value7: ""
  };
},
mounted: function() {
  this.getSelect();
  this.list = this.states.map(item => {
    return { value: item, label: item };
  });
      this.session = getsession(this.$router);
      this.formSearch.creater = this.session;
      this.getTable();
},
methods: {
  querySearch(queryString, cb) {
    var restaurants = this.restaurants;
    var results = queryString
      ? restaurants.filter(this.createFilter(queryString))
      : restaurants;
    // 调用 callback 返回建议列表的数据
    cb(results);
  },
  createFilter(queryString) {
    return restaurant => {
      return (
        restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
        0
      );
    };
  },
  getsession() {
    if (sessionStorage.getItem("name")) {
      this.session = sessionStorage.getItem("name");
      this.formSearch.creater = this.session;
    } else {
      this.$router.push("/login");
    }
  },
  editRoll(index, row) {
    console.log(index);
    console.log(row);
    this.edittable = true;
  },
  handleSelect(item) { 
    console.log(item);
  },
  handleSizeChange(val) {
    console.log(`每页 ${val} 条`);
    this.searchcCndetion={
        page:"1", rows:val,  type:"",  name:"",  user:"",  custom:""
    }
    this.getTable();
  },
  handleCurrentChange(val) {
    console.log(`当前页: ${val}`);
    this.searchcCndetion={
        page:val, rows:"20",  type:"",  name:"",  user:"",  custom:""
    }
    this.getTable();
  },
  deleteTab() {
    const tthis = this;
    if (this.multipleSelection.length < 1) {
      this.$message.error("请选择一条数据进行操作！");
    } else {
      const dataArr = [];
      for(let i=0; i<this.multipleSelection.length; i++) {
          dataArr.push(this.multipleSelection[i].model_id);
      }
      const deInfo = {
        model_id: dataArr
      };
      this.$http.post(`${process.env.VUE_APP_BASE_API}kwe/index/deleteModel`, deInfo).then(resp => {
        console.log(resp);
        if (resp.data.status == "200") {
          tthis.getTable();
           tthis.$message({
            showClose: true,
            message: "删除成功",
            type: "success"
          });
        } else {
          tthis.$message({
            message: "删除失败",
            type: "error"
          });
        }
      });
    }
  },
  editYw() {
    const tthis = this;
    if (this.multipleSelection.length != 1) {
      this.$message.error("请选择一条数据进行操作！");
    } else {
      this.dialogFormVisibleEdit = true;
      this.formYwEdit = this.multipleSelection[0];
      this.formYwEdit.updateUser = this.session;
    }
  },
  eEditYw() {
    const tthis = this;
    console.log(this.formYwEdit);
    if (this.formYwEdit.type == "进仓") {
      this.formYwEdit.type = 0;
    } else {
      this.formYwEdit.type = 1;
    }
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/updateYW`, this.formYwEdit)
      .then(function(res) {
        console.log(res);
        if (res.data == "updateNice") {
          tthis.getTable;
          tthis.$message({
            showClose: true,
            message: "修改成功",
            type: "success"
          });
          tthis.dialogFormVisibleEdit = false;
          tthis.getTable();
        } else {
          tthis.$message({
            message: "修改失败",
            type: "error"
          });
        }
      });
  },
  editcencal() {
     this.dialogFormVisibleNew = false 
  },
  handleSelectionChange(val) {
    this.multipleSelection = val;
  },
  goEdit(index, row) {
    if (row.remarks2 == "进仓") {
      this.$router.push({
        path: "/ImportEnteringTemplate",
        name: "ImportEnteringTemplate",
        params: row,
        query: row
      });
    } else if (row.remarks2 == "出仓") {
      this.$router.push({
        path: "/ExportEnteringTemplate",
        name: "ExportEnteringTemplate",
        params: row,
        query: row
      });
    }
  },
  handleClose(done) {
    this.$confirm("确认关闭？")
      .then(_ => {
        done();
      })
      .catch(_ => {}); 
  },
  getTable() {
    const tthis = this;
    this.$http
      .post(
        `${process.env.VUE_APP_BASE_API}kwe/index/modelList`, this.searchcCndetion)
      .then(function(response) {
        console.log(response);
        tthis.tableData3 = response.data.rows;
        tthis.tableData3.forEach(res => {
          if (res.remarks2 == 0) {
            res.remarks2 = "进仓";
          } else {
            res.remarks2 = "出仓";
          }
        });
      })
      .catch(function(error) {
        console.log(error);
      });
  },
  getSelect() {
    const tthis = this;
    (tthis.loading = true),
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/searchCustom`)
        .then(function(response) {
          console.log(response);
          tthis.options4 = response.data;
          tthis.loading = false;
        })
        .catch(function(error) {
          tthis.loading = false;
          console.log(error);
        });
  },
  onSubmit() { 
    this.searchcCndetion={
        page:"1", rows:"20",  type:this.formSearch.yewuType,  name:this.formSearch.name,  user:this.formSearch.creater,  custom:this.formSearch.custName
    }
    console.log(this.searchcCndetion);
    this.getTable();
  },
  submitNewYewu() {
    console.log(this.formInline);
    this.formInline.create_user = this.session;
    const tthis = this;
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/createModel`, this.formInline)
      .then(function(resp) {
        tthis.dialogFormVisibleNew = false;
        if(resp.data != "saveError") {
                tthis.$message({
                    message: "新建模板成功",
                    type: "success"
                  });
            tthis.getTable();
        } else {
                tthis.$message({
                message: "创建失败",
                type: "error"
              });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  }
}
};
</script>

<style>
html,
body {
height: 100%;
}
.pagination {
text-align: right;
margin-top: 10px;
}
</style>

