  <template>
  <div class="app-container"  v-loading="loadingOcr"
    element-loading-text="正在上传中"
    element-loading-spinner="el-icon-loading"
    >
    <div>
      <el-button type="primary" size="mini" plain @click="onSubmit" icon="el-icon-search">查询</el-button>
      <el-button type="primary" size="mini" plain @click="editYw()">修改</el-button>
      <el-button type="primary" size="mini" plain @click="copylist()">复制单证</el-button>
      <el-button type="primary" size="mini" plain @click="dialogVisible = true">
        上传文件
        <i class="el-icon-upload el-icon--right"></i>
      </el-button>
      <!-- <el-button @click="dialogVisible = true">数据导出</el-button> -->
      <el-button type="primary" size="mini" plain @click="dialogFormVisibleNew = true">创建业务</el-button>
      <!-- <el-button @click="dialogVisible = true">单证打印</el-button> -->
      <el-button type="primary" size="mini" plain @click="exportList()">导出报表</el-button>
      <el-button type="danger" size="mini" plain @click="deleteTab()">删除</el-button>
    </div>
    <el-form :inline="true" :model="formSearch" class="demo-form-inline" style="margin: 20px 0" size="mini">
      <el-row :gutter="20">
        <el-col :span="6" >
          <el-form-item label="业务编号" >
            <el-input v-model="formSearch.onlyNo" placeholder="业务编号" size="mini"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="唯一编号">
            <el-input v-model="formSearch.yewuNo" placeholder="唯一编号" size="mini"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="客户名称"> 
          <el-select
            v-model="formSearch.custName"
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
        </el-col>
        <el-col :span="6">
          <el-form-item label="创建人">
            <el-input v-model="formSearch.creater" placeholder="创建人" size="mini"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="业务状态">
            <!-- <el-input v-model="formSearch.yewuState" placeholder="业务状态" size="mini"></el-input> -->
             <el-select v-model="formSearch.yewuState" clearable placeholder="请选择" size="mini">
              <el-option
                v-for="item in startType"
                :key="item.label"
                :label="item.label"
                :value="item.label"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="6">
          <el-form-item label="接单备注">
            <el-input v-model="formSearch.jiedanRemark" placeholder="接单备注" size="mini"></el-input>
          </el-form-item>
        </el-col> -->
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

        <el-col :span="10">
          <el-form-item>
            <span class="demonstration">起始时间</span>
            <el-date-picker
              v-model="formSearch.date"
              type="daterange"
              align="right"
              unlink-panels
              formagt="yyyy-MM-dd"
              size="mini"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions2"
            ></el-date-picker>
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
      <el-table-column prop="type" label="类型" width="80px"></el-table-column>
      <el-table-column prop="customNo" label="客户业务编号" width="160px"></el-table-column>
      <el-table-column prop="clearingCustomerName" label="客户名称" width="150px"></el-table-column>
      <el-table-column prop="ywNo" label="唯一编号" width="250px"></el-table-column>
      <el-table-column prop="status" label="状态" show-overflow-tooltip width="120px">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '未录入'" style="color:red">{{ scope.row.status }}</span>
          <span v-else style="color: #606266">{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createUserEmp" label="创建人" width="100px"></el-table-column>
      <el-table-column prop="createDate" label="创建时间" width="180px">
      </el-table-column>
      <el-table-column prop="updateUserEmp" label="更新人" width="100px"></el-table-column>
            <el-table-column prop="updateDate" label="更新时间" width="180px">
        <template slot-scope="scope">{{moment(scope.row.updateDate).format("YYYY-MM-DD HH:mm:ss")}} </template>
      </el-table-column>
      <el-table-column prop="auditUserEmp" label="审核人" width="100px"></el-table-column>

      <el-table-column prop="remark" label="备注" show-overflow-tooltip width="180px"></el-table-column>
      <el-table-column prop="remark4" label="识别引擎"  width="80px"></el-table-column>
      <el-table-column prop="templateName" label="识别模板"  width="180px"></el-table-column>
      <el-table-column prop="modelName" label="客户模板"  width="180px"></el-table-column>
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
          :total="tableData3Tot*1"
        ></el-pagination>
      </div>
    </el-row>
    <!-- 上传的digloag -->
 <el-dialog title="识别上传" :visible.sync="dialogVisible" width="30%"
>
  <el-form :model="uplodForm" size="small"  :rules="rulesOcr" ref="uplodForm">
    <el-form-item label="结算客户编号" :label-width="formLabelWidth1" prop="customerNo">
      <el-input v-model="uplodForm.customerNo" autocomplete="off" ref='idInput' @keyup.native="getOcrtype"></el-input>
    </el-form-item>
        <el-form-item label="客户名称" :label-width="formLabelWidth1" >
          <el-select
            v-model="uplodForm.customKey"
            filterable
            placeholder="请选择"
            style="width:100%"
            @change="getDivision(uplodForm.customerNo, uplodForm.customKey)"
          >
            <el-option
              v-for="item in options4"
              :key="item.ecustomid"
              :label="item.customName"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单证类型" :label-width="formLabelWidth1" v-if="showOcrtype"> 
          <el-select v-model="uplodForm.type" placeholder="单证类型" style="width:100%">
            <el-option label="进仓" value="0"></el-option>
            <el-option label="出仓" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item  label="识别引擎" :label-width="formLabelWidth1">
          <el-select
          @change="getOcrSdk(uplodForm)"
           v-model="uplodForm.ocrUrl"  placeholder="识别引擎" style="width:100%">
            <el-option label="SDK" value="SDK"></el-option>
            <el-option label="SDX" value="SDX"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户模板" prop="remark2" :label-width="formLabelWidth1">
          <el-select
            v-model="uplodForm.templateID"
            filterable
            placeholder="请选择"
            style="width:100%"
          >
            <el-option
              v-for="item in templatelist"
              :key="item.model_id"
              :label="item.remarks1"
              :value="item.model_id"
            ></el-option>
          </el-select>
        </el-form-item>
         <el-form-item v-if="ocrShowFlog" label="识别模板" :label-width="formLabelWidth1">
          <el-select
            v-model="uplodForm.ocrTemplateID"
            filterable
            placeholder="请选择"
            style="width:100%"
          >
            <el-option
              v-for="item in ocrlist"
              :key="item.id"
              :label="item.customkey + '['+item.name+']'"
              :value="item.templateid"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="文件" :label-width="formLabelWidth1">
          <input  type="file"  id="fileInput" />
        </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary"  @click="submitOcr()">确 定</el-button>
  </div>
</el-dialog>
    <!-- 新建单证的dialogvisible -->
    <el-dialog title="新建业务" :visible.sync="dialogFormVisibleNew" width="35%">

      <el-form :model="formInline" :rules="rules" ref="formInline" label-width="140px" size="small"> 
               <el-form-item label="结算客户编号"  prop="customNo">
          <el-input v-model="formInline.customNo" autocomplete="off" @keyup.native="getcustomNo"></el-input>
      </el-form-item>
        <el-form-item label="客户名称" prop="clearingCustomerName">
          <el-select
            v-model="formInline.clearingCustomerName"
            filterable
            placeholder="请选择"
            style="width:100%"
          >
            <el-option
              v-for="item in options4"
              :key="item.ecustomid"
              :label="item.customName"
              :value="item.customName"
              @click.native="getDivision(formInline.customNo, item.key)"
            ></el-option>
          </el-select>
        </el-form-item >
         <el-form-item label="模板名称" prop="remark2">
          <el-select
            v-model="formInline.remark2"
            filterable
            placeholder="请选择"
            style="width:100%"
          >
            <el-option
              v-for="item in templatelist"
              :key="item.model_id"
              :label="item.remarks1"
              :value="item.model_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单证类型" prop="type"  v-if="showType">
          <el-select v-model="formInline.type" placeholder="单证类型" style="width:100%">
            <el-option label="进仓" value="0"></el-option>
            <el-option label="出仓" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接单备注">
          <el-input v-model="formInline.remark" autocomplete="off"></el-input>
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
import { constants } from 'crypto';
export default {
  name: "todo",
  data() {
    return {
      multipleTable: [],
      multipleSelection: [],
      dialogVisible: false,
      dialogFormVisibleNew: false,
      dialogFormVisibleEdit: false,
      tableData3: [],
      session: "",
      ocrShowFlog: false,
      formLabelWidth: "80px",
      formLabelWidth1: "130px",
      loading: false,
      state2: "",
      ocrFile:"",
      showType: false,
      showOcrtype: false,
      formYwEdit: {},
      uplodForm:{
        customKey  : "",
        customerNo : "",
        type:"",
        remark:"",
        serviceFile: "",
        createUser: "",
        templateID: "",
        ocrTemplateID: "",
        ocrUrl: ""

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
       startType: [
        {
          value: 1,
          label: "待审核"
        },
        {
          value: 2,
          label: "已审核"
        },
        {
          value: 3,
          label: "已同步"
        },
        {
          value: 4,
          label: "已完成"
        },
        {
          value: 5,
          label: "已修改"
        },
        {
          value: 6,
          label: "待修改"
        }
      ],
      formInline: {
        clearingCustomerName: "",
        remark: "",
        type: "",
        customNo: "",
        createUser: "",
        remark2: ""
      },
      Newyewu: {
        custname: ""
      },
      formSearch: {
        onlyNo: "",
        yewuNo: "",
        custName: "",
        creater: "",
        date: "",
        yewuState: "",
        yewuType: "",
        jiedanRemark: ""
      },
      rulesOcr: {
           customerNo: [ 
            { required: true, message: '不能为空', trigger: 'blur' },
            { min: 15, message: '编号输入不合法', trigger: 'blur' },
            {pattern: /(^\S{15}$)|(^\S{15})+(\-[0-9]*)?$/, message: '编号输入不合法'}
          ]
      },
      rules:{
          clearingCustomerName: [{
            required: true, message: '不能为空', trigger: 'blur'
          }],
          remark2: [{
            required: true, message: '不能为空', trigger: 'blur'
          }],
          type: [{
            required: true, message: '不能为空', trigger: 'blur'
          }],
          customNo:[ 
            { required: true, message: '不能为空', trigger: 'blur' },
            { min: 15, message: '编号输入不合法', trigger: 'blur' },
            {pattern: /(^\S{15}$)|(^\S{15})+(\-[0-9]*)?$/, message: '编号输入不合法'}
          ]
      },
      yewuState:'',
      options4: [],
      templatelist: [],
      value9: [],
      loadingOcr: false,
      list: [],
      ocrlist:[],
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
      custid: "",
      searchDate: "",
      tableData3Tot:"",
      currentPage4: 1,
      offset: 1,
      limit: 20,
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
        this.getTable( 
        this.formSearch.yewuState,
        this.formSearch.date,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        1,
        20,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1,
        this.formSearch.custName
    );
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
      this.edittable = true;
    },
    handleSelect(item) { 
      console.log(item);
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.getTable(
        this.formSearch.yewuState,
        this.searchDate,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        1,
        val,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1,
        this.formSearch.custName
      );
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.getTable(
        this.formSearch.yewuState,
        this.searchDate,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        val,
        20,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1,
        this.formSearch.custName
      );
    },
    deleteTab() {
      const tthis = this;
      if (this.multipleSelection.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
            this.$confirm(`确定删除${this.multipleSelection.length}条数据吗？`, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
              }).then(() => {
               const dataArr = [];
              for(let i=0; i<this.multipleSelection.length; i++) {
                if (this.multipleSelection[i].status !== "待审核" && !this.multipleSelection[i].status) {
                  tthis.$message({
                    message: "只能删除审核前单证",
                    type: "error"
                  });
                  return;
                } else {
                  dataArr.push(this.multipleSelection[i].ywNo);
                }
              }
              const deInfo = {
                ywlist: dataArr,
                opUser: this.session
              };
              this.$http.post(`${process.env.VUE_APP_BASE_API}kwe/index/ywDrop`, deInfo).then(resp => { 
                console.log(resp);
                if (resp.data == "success") {
                  tthis.getTable(
                      tthis.formSearch.yewuState,
                      tthis.formSearch.date,
                      tthis.formSearch.onlyNo,
                      tthis.formSearch.yewuNo,
                      1,
                      20,
                      tthis.formSearch.creater,
                      tthis.formSearch.yewuType,
                      1,
                      this.formSearch.custName
                  );
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
              }).catch(() => {
                  console.log("cancel");
          });
      }
    },
    getcustomNo() {
      if(this.formInline.customerNo .substring(0,4) == "SKLB" ) {
          console.log("SKLB");
          this.showType = true;
          
      } else {
        this.showType = false;
      }
    },
    getOcrtype() {
      if(this.uplodForm.customerNo.substring(0,4) == "SKLB" ) {
          console.log("SKLB");
          this.showOcrtype = true;
          
      } else {
        this.showOcrtype = false;
      }
    },
    editYw() {
      const tthis = this;
      if (this.multipleSelection.length != 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
        this.dialogFormVisibleEdit = true;
        this.formYwEdit = this.multipleSelection[0];
        console.log(this.formYwEdit);
        this.formYwEdit.updateUser = this.session;
      }
    },
    eEditYw() {
      this.loadingOcr = true;
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
            tthis.loadingOcr = false;
            tthis.getTable;
            tthis.$message({
              showClose: true,
              message: "修改成功",
              type: "success"
            });
            tthis.dialogFormVisibleEdit = false;
            tthis.getTable(
                tthis.formSearch.yewuState,
                tthis.formSearch.date,
                tthis.formSearch.onlyNo,
                tthis.formSearch.yewuNo,
                1,
                20,
                tthis.formSearch.creater,
                tthis.formSearch.yewuType,
                1,
                this.formSearch.custName
            );
          } else {
            tthis.loadingOcr = false;
            tthis.$message({
              message: "修改失败",
              type: "error"
            });
          }
        });
    },
    getDivision(customerNo, customKey, type) { 
      if(!customerNo) {
        customKey = ""
        this.$message({
              message: "请先填写业务编号",
              type: "error"
              });
          this.uplodForm = {};
          this.formInline = {};
          this.templatelist = [];
          this.ocrlist = [];
        return;
      } else {
       const subcusttype = customerNo.substring(0,4);
        console.log(subcusttype);
        if(subcusttype == "SKLI") {
          var uplodType = "0"
        } else if(subcusttype == "SKLO") {
          var uplodType = "1"
        } else if(subcusttype == "SKLB"){
          var uplodType = "";
        } else{
          // alert('客户编号不合法');
          this.$message({
                          message: "客户编号不合法，请重新输入",
                          type: "error"
                        })
          this.$refs.idInput.focus();
          this.uplodForm = {};
          this.formInline = {};
          this.templatelist = [];
          this.ocrlist = [];
          return; 
        }
        console.log(customKey,uplodType)
        this.searchmodelList(customKey, uplodType)
      }
    },
    searchmodelList(custom, type) {
      const tthis = this;
        this.$http
          .post(`${process.env.VUE_APP_BASE_API}kwe/index/modelList`,{page:"1", rows:"1000",  type: type,  name: "",  user:"",  custom: custom})
          .then(function(response) {
            tthis.templatelist = response.data.rows;
            tthis.loading = false;
          })
          .catch(function(error) {
            tthis.loading = false;
            console.log(error);
          });
          this.$http
            .post(
            `${process.env.VUE_APP_BASE_API}kwe/index/getOcrList`, {
                page:"1", rows:"2000",  remarks1:type,  name:"",  user:"",  custom: custom
            })
            .then(function(response) {
              tthis.ocrlist = response.data.rows;
            })
            .catch(function(error) {
              console.log(error);
            });
    },
    getOcrSdk(form) {
      console.log(form);
        if(!form.customerNo) {
        this.$message({
              message: "请先填写业务编号",
              type: "error"
              });
          this.uplodForm = {};
          this.formInline = {};
          this.templatelist = [];
          this.ocrlist = [];
        return;
      } else {
       const subcusttype = form.customerNo.substring(0,4);
        console.log(subcusttype);
        if(subcusttype == "SKLI") {
          var uplodType1 = "0"
        } else if(subcusttype == "SKLO") {
          var uplodType1 = "1"
        } else if(subcusttype == "SKLB"){
          var uplodType1 = "";
        } else{
          // alert('客户编号不合法');
          this.$message({
                          message: "客户编号不合法，请重新输入",
                          type: "error"
                        })
          this.$refs.idInput.focus();
          this.uplodForm = {};
          this.formInline = {};
          this.templatelist = [];
          this.ocrlist = [];
          return; 
        }}
      this.uplodForm.ocrUrl == 'DG' ? this.ocrShowFlog = false : this.ocrShowFlog = true
      console.log(form);
      const tthis = this;
            this.$http
            .post(
            `${process.env.VUE_APP_BASE_API}kwe/index/getOcrList`, {
                page:"1", rows:"2000",  remarks1:uplodType1,  name:"",  user:"",  custom: form.customKey ,remarks2: form.ocrUrl
            })
            .then(function(response) {
              tthis.ocrlist = response.data.rows;
            })
            .catch(function(error) {
              console.log(error);
            });
    },
    editcencal() {
       this.dialogFormVisibleNew = false 
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    goEdit(index, row) {
      if (row.type == "出仓") {
        this.$router.push({
          path: "/ExportEntering",
          name: "ExportEntering",
          params: row,
          query: row
        });
      } else if (row.type == "进仓") {
        this.$router.push({
          path: "/ImportEntering",
          name: "ImportEntering",
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
    getTable(status, startDate, customNo, ywNo, offset, limit, op_user,type,remark10, custid) {
      const tthis = this;
      this.$http
        .get(
          `${process.env.VUE_APP_BASE_API}kwe/index/ywList?status=${status}&dateStr=${startDate}&customNo=${customNo}&ywNo=${ywNo}&current=${offset}&size=${limit}&createUser=${op_user}&type=${type}&remark10=${remark10}&clearingCustomerName=${custid}`
        )
        .then(function(response) {
          console.log(response);
          tthis.tableData3 = response.data.ywList.records;
          tthis.tableData3Tot = response.data.ywList.total;
          tthis.tableData3.forEach(res => {
            if (res.type == 0) {
              res.type = "进仓";
            } else {
              res.type = "出仓";
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
          this.$http
          .post(`${process.env.VUE_APP_BASE_API}kwe/index/modelList`,{page:"1", rows:"1000",  type:"",  name:"",  user:"",  custom:""})
          .then(function(response) {
            console.log(response);
            tthis.templatelist = response.data.rows;
            tthis.loading = false;
          })
          .catch(function(error) {
            tthis.loading = false;
            console.log(error);
          });
          this.$http
            .post(
              `${process.env.VUE_APP_BASE_API}kwe/index/getOcrList`, {
        page:"1", rows:"2000",  remarks1:"",  name:"",  user:"",  custom:""
    })
            .then(function(response) {
              tthis.ocrlist = response.data.rows;
            })
            .catch(function(error) {
              console.log(error);
            });
    },
    onSubmit() { 
      if (this.formSearch.date[0]) {
        this.searchDate = `${this.moment(this.formSearch.date[0]).format(
          "YYYY-MM-DD"
        )}||${this.moment(this.formSearch.date[1]).format("YYYY-MM-DD")}`;
      } else {
        this.searchDate = "";
      }
      console.log(this.formSearch);
      this.getTable(
        this.formSearch.yewuState,
        this.searchDate,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        1,
        20,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1,
        this.formSearch.custName
      );
    },
    submitNewYewu() {
          this.$refs.formInline.validate((valid) => {
          if (valid) {
                  console.log(this.formInline);
          this.formInline.createUser = this.session;
          var searchDate = "";
          if (this.formSearch.date[0]) {
            searchDate = `${this.moment(this.formSearch.date[0]).format(
              "YYYY-MM-DD"
            )}||${this.moment(this.formSearch.date[1]).format("YYYY-MM-DD")}`;
          } else {
            searchDate = "";
          }
        if(this.formInline.customNo.substring(0,4) == "SKLI") {
          this.formInline.type = "0"
        } else if(this.formInline.customNo.substring(0,4) == "SKLO" || this.formInline.customNo.substring(0,4) == "SKLB") {
          this.formInline.type = "1"
        } else if(this.formInline.customNo.substring(0,4) == "SKLB") {
          this.formInline.type = this.formInline.type
        } else {
          alert('客户编号不合法');
          return; 
        }
          const tthis = this;
          this.$http
            .post(`${process.env.VUE_APP_BASE_API}kwe/index/addYW`, this.formInline)
            .then(function(resp) {
              console.log(666666);
              console.log(resp)
              tthis.getTable(
                    tthis.formSearch.yewuState,
                    tthis.formSearch.date,
                    tthis.formSearch.onlyNo,
                    tthis.formSearch.yewuNo,
                    1,
                    20,
                    tthis.formSearch.creater,
                    tthis.formSearch.yewuType,
                    1,
                    tthis.formSearch.custName
              );
              tthis.dialogFormVisibleNew = false;
              if(resp.data.status == "200") {
                      tthis.$message({
                          message: "新建业务成功",
                          type: "success"
                        });
              } else {
                    tthis.$message({
                    showClose: true,
                    duration:0,
                    message: resp.data.message,
                    type: "error"
                  });
              }
            })
            .catch(function(error) {
              console.log(error);
            });
              } else {
                console.log('error submit!!');
                return false;
              }
            });
    },
    copylist() {
      const tthis = this;
      if (this.multipleSelection.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
        console.log(this.multipleSelection[0].ywNo);
         this.$http
        .post(`${process.env.VUE_APP_BASE_API}kwe/index/SaveAs`, {
          ywNo: this.multipleSelection[0].ywNo
        }).then(resp=>{
              console.log(resp)
              if(resp.data.status = "200") {
                tthis.$message({ 
                showClose: true,
                message: resp.data.message,
                type: "success"
              });
                tthis.getTable(
                tthis.formSearch.yewuState,
                tthis.formSearch.date,
                tthis.formSearch.onlyNo,
                tthis.formSearch.yewuNo,
                1,
                20,
                tthis.formSearch.creater,
                tthis.formSearch.yewuType,
                1,
                tthis.formSearch.custName
          );
              }  else{
                 tthis.$message({ 
                showClose: true,
                duration:0,
                message: resp.data.message,
                type: "error"
              });
              }
        }).catch(err => {
              tthis.$message({ 
                showClose: true,
                duration:0,
                message: err,
                type: "error"
              });
        })
      }
    },
    submitOcr() {
        const tthis = this; 
        this.uplodForm.createUser = this.session;
        this.uplodForm.serviceFile = document.getElementById("fileInput").files[0];
        if(this.uplodForm.customerNo.substring(0,4) == "SKLI") {
          this.uplodForm.type = "0"
        } else if(this.uplodForm.customerNo.substring(0,4) == "SKLO" ||  this.uplodForm.customerNo.substring(0,4) == "SKLB") {
          this.uplodForm.type = "1"
        } else if(this.uplodForm.customerNo.substring(0,4) == "SKLB"){
          this.uplodForm.type = this.uplodForm.type
        }else{
          alert('客户编号不合法');
          return; 
        }
          let formdata = new FormData();
          formdata.append('serviceFile',document.getElementById("fileInput").files[0]);
          formdata.append('createUser',this.session);
          formdata.append('customKey',this.uplodForm.customKey);
          formdata.append('customerNo',this.uplodForm.customerNo);
          formdata.append('ocrTemplateID',this.uplodForm.ocrTemplateID);
          formdata.append('ocrUrl',this.uplodForm.ocrUrl);
          formdata.append('templateID',this.uplodForm.templateID);
          formdata.append('type',this.uplodForm.type);
          console.log(this.uplodForm);
          let config = {
            headers: {
                'Content-Type': 'multipart/form-data'  //之前说的以表单传数据的格式来传递fromdata
            }
        };
        this.$http
        .post(`${process.env.VUE_APP_BASE_API}kwe/ocr/addFileYW`, formdata, config).then(resp=>{
              console.log(resp)
              if(resp.data.status == "200") {
                    tthis.$message({ 
                    showClose: true,
                    message: resp.data.message,
                    type: "success"
                  });
                tthis.dialogVisible = false;
                 tthis.getTable(
                tthis.formSearch.yewuState,
                tthis.formSearch.date,
                tthis.formSearch.onlyNo,
                tthis.formSearch.yewuNo,
                1,
                20,
                tthis.formSearch.creater,
                tthis.formSearch.yewuType,
                1,
                tthis.formSearch.custName
          );
              } else {
                    tthis.$message({ 
                      showClose: true,
                      duration:0,
                      message: resp.data.message,
                      type: "error"
                    });
              }
              }
        ).catch(err => {
              tthis.$message({ 
                showClose: true,
                duration:0,
                message: err,
                type: "error"
              });
        })
      
    },
    exportList() {    
        this.$http
        .post(`${process.env.VUE_APP_BASE_API}kwe/index/downloadList`,
        {
          "status" :this.formSearch.yewuState,
          "dateStr": this.formSearch.date,
          "customNo": this.formSearch.onlyNo,
          "ywNo":  this.formSearch.yewuN,
          "createUser": this.formSearch.creater,
          "type": this.formSearch.yewuType,
          "clearingCustomerName": this.formSearch.custName
        }
        ).then(resp=>{
          console.log(resp);
          // location.href = `${process.env.VUE_APP_BASE_API}kwe/index/downloadList?status=${this.formSearch.yewuState}&dateStr=${this.formSearch.date}&customNo=${this.formSearch.onlyNo}&ywNo=${this.formSearch.yewuNo}&current=''&size=''&createUser=${this.formSearch.creater}&type=${this.formSearch.yewuType}&remark10=''&clearingCustomerName=${this.formSearch.custName}`;
        }).catch(err=>{
          console.log(err);
        })
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

