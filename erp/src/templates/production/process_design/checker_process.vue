<template>
	<div id="checker_product_design">
		<router-view @update="getCheckerProcessList"></router-view>
		<!-- 
			 default-sort  默认的排序 是一个对象
			 属性:prop:指定的排序列名 order:排序方式
			 有 ascending or descending
			 -->
		<!--@row-dblclick :default-sort="sort_regulation" -->
		<el-table @row-dblclick="viewProcessDetail" :border="true" ref="product_table" :highlight-current-row="true" style="width: 100%" :data="search"
		 :default-sort="{prop: 'process_id', order: 'descending'}">
			<el-table-column label="工序设计单编号" prop="process_id" sortable>
				<template slot-scope="scope">
					{{scope.row.process_id}}
				</template>
			</el-table-column>
			<el-table-column label="产品编号" prop="product_id" sortable>
				<template slot-scope="scope">
					{{scope.row.product_id}}
				</template>
			</el-table-column>
			<el-table-column label="产品名称" prop="product_name" >
				<template slot-scope="scope">
					{{scope.row.product|whetherShow('product_name')}}
				</template>
			</el-table-column>
			<el-table-column label="产品类别名称" prop="product_name" >
				<template slot-scope="scope">
					{{scope.row.product|whetherShow('kindName')}}
				</template>
			</el-table-column>
			<el-table-column label="用途类型" prop="use_type">
				<template slot-scope="scope">
					{{scope.row.product|whetherShow('useName')}}
				</template>
			</el-table-column>
			<el-table-column label="设计人" prop="cost_price" >
				<template slot-scope="scope">
					{{ scope.row|whetherShow('designer')}}
				</template>
			</el-table-column>
			<el-table-column label="设计要求" prop="cost_price" >
				<template slot-scope="scope">
					{{ scope.row|whetherShow('message','无设计要求')}}
				</template>
			</el-table-column>
			<el-table-column align="right" width="100px">
				<template slot="header" slot-scope="scope">
					<el-input v-model="keyWord" placeholder="输入搜索关键字" />
				</template>
				<template slot-scope="scope">
					<el-button type="primary" title="核审产品工序组成设计" @click="checkerProcess(scope.row.id)" icon="el-icon-edit" circle ></el-button>
					<!-- <el-button type="primary" size="small" title="审核" @click="checkerProcess(scope.row.id)">审核</el-button> -->
				</template>
			</el-table-column>
		</el-table>
		<div v-show="showDetail">
			<h6>工序设计详细单:<span style="color: red;">{{activeProcess.process_id}}</span></h6>
			<process-design-detail :id="activeProcess.id"></process-design-detail>
		</div>
	</div>
</template>

<script>
	import process_design_detail from '@/components/product/process_design_detail.vue'
	export default {
		name: 'checker_process',
		data() {
			return {
				processList: [],
				keyWord:'',
				activeProcess:{id:''},
				showDetail:false
			}
		},
		created() {
			this.getCheckerProcessList()
		},
		methods: {
			getCheckerProcessList() { //获取需要审核的process
				this.$axios.get('/api/processDesign/checker')
					.then(response => {
						this.processList = response.data
					})
			},viewProcessDetail(data){//查看工序设计单的详细信息
				if(data.id==this.activeProcess.id){
					this.showDetail=!this.showDetail
				}else{
					this.showDetail=true
					this.activeProcess=JSON.parse(JSON.stringify(data));
				}
				
			},checkerProcess(id){
				let fullPath=this.$route.fullPath
				this.$router.push({path:`${fullPath}/checker/${id}`})
			}
		},computed:{
			search() {
				let keyWord=this.keyWord
				if (keyWord == '') {
					return this.processList
				} else {
					return this.processList.filter(process => {
						return process.process_id.includes(keyWord) ||
							process.product.product_name.includes(keyWord) ||
							process.product_id.includes(keyWord)
					})
				}
			}
		},filters:{
			whetherShow(obj, property, message) {
				if (message == undefined || message == '')
					message = "无"
				if (obj == '' || obj == null) {
					return message;
				}
				if (obj[property] == '' || obj[property] == null)
					return message;
				return obj[property];
			},
			formNumerical(numerical,suffix) {
				//保留两位小数
				if(suffix){
					return parseInt(numerical).toFixed(2)+suffix;
				}
				return parseInt(numerical).toFixed(2);
			}
		},components:{
			'process-design-detail':process_design_detail
		}
	}
</script>

<style>
</style>
