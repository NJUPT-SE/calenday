<template>
	<!-- 本页面 -->
	<view class="bg-white">
		<!-- 功能按钮st -->
		<view class="flex padding-top padding-right justify-end">
			<button class="cu-btn light bg-orange shadow-lg" @tap="createBigDay">
				<text class="cuIcon-add"></text> 新建
			</button>
		</view>
		<!-- 功能按钮ed -->

		<!-- 时间轴st -->
		<view class="padding">
			<view class="cu-timeline" v-for="dayInfo in bigDays" :key="dayInfo.id" >

				<!-- 日期 -->
				<view class="cu-time">{{dayInfo.date}}</view>

				<!-- 图标 -->
				<view class="cu-item" :class="'text-'+colorList[dayInfo.id%9]">

					<!-- 内容 -->
					<view 
					class="content light" :class="'bg-'+colorList[dayInfo.id%9]" 
					v-for="i in dayInfo.title.length" 
					:key="i"
					@longpress="del(dayInfo.id,i)" data-target="DialogModal"
					>
						<text>【{{dayInfo.title[i]}}】{{dayInfo.notes[i]}}</text>
					</view>
				</view>


			</view>
		</view>

		<!-- 时间轴ed -->

		<!-- 删除弹框st -->
		<view class="cu-modal" :class="modalName=='DialogModal'?'show':''">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content text-red">提醒</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<view class="padding-xl">
					您要将该回忆...？
					<!-- 传给后端的值：uid={{uid}} id={{selectedId}} index={{selectedIndex}} -->
				</view>
				<view class="cu-bar bg-white col-2">
					<view class="padding">
						<button class="cu-btn bg-green " @tap="comfirmEdit">回炉重造</button>
					</view>
					<view class="padding">
						<button class="cu-btn bg-red margin-left" @tap="comfirmDel">忍痛丢弃</button>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 删除弹框ed -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				uid:1,
				modalName: null,
				colorList: ["grey","red", "green", "purple", "blue", "orange", "yellow", "olive", "cyan", "brown"],
				bigDays: [{
						id: 1,
						date: "01-01",
						title:["赖美云跨年晚会"],	
						notes: ["好开心啊"]
					},
					{
						id: 2,
						date: "01-10",
						title:["操作系统复习了一晚上","期末考试结束"],
						notes: ["加油！","Yeah！"]
					},
					{
						id: 3,
						date: "01-11",
						title:["寒假正式开始"],
						notes: ["回到家了"]
					},
					{
						id: 4,
						date: "01-25",
						title: ["又是一年除夕夜"],
						notes:["新年快乐！"]
						
					},
					{
						id: 5,
						date: "02-14",
						title: ["情人节——我还在看高木同学","高木同学（二）","高木同学（三）"],
						notes:["好酸","好甜","我可以"]
					},
					{
						id: 6,
						date: "02-29",
						title: ["2020年2月29日"],
						notes:["四年一度，打卡留念！"]
					},
					{
						id: 7,
						date: "03-15",
						title: ["酸掉牙"],
						notes:["甜到需要胰岛素"]
					},
					{
						id: 8,
						date: "05-01",
						title: ["劳动节"],
						notes:["劳动最快乐！"]
					},
					{
						id: 9,
						date: "05-02",
						title: ["炮姐生日"],
						notes:["Bilibili干杯！"]
					},
					{
						id: 10,
						date: "05-13",
						title: ["任务完成"],
						notes:["点滴-接口登录对接成功！"]
					},
				],
				selectedId:-1,
				selectedIndex:-1,
			}
		},
		methods: {
			createBigDay:function(){
				uni.navigateTo({
					animationType:"slide-in-right",
					url:"./createBigDay"
				})
			},
			showModal: function(e) {
				this.modalName = "DialogModal";
			},
			
			hideModal: function(e) {
				this.modalName = null
			},
			del:function(dateId,eventId){
				this.showModal();
				this.selectedId = dateId;
				this.selectedIndex = eventId;
				
			},
			comfirmEdit:function(){
				console.log("edit: ",this.selectedId,this.selectedIndex);
				this.hideModal();
			},
			comfirmDel:function(){
				console.log("delete: ",this.selectedId,this.selectedIndex);
				this.hideModal();
			},
		}
	}
</script>

<style>

</style>
