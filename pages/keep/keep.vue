<template>
	<view class="bg-white">
		
		<!-- 展示习惯打卡列表st -->
		<view class="bg-white padding">
			<!-- 晨间 -->
			<text class="text-black text-xl text-bold">晨间</text>
			<view class="bg-white padding">
				<view class="grid col-5 grid-square">
					<view class="cu-avatar lg bg-white" v-for="item in keepData" v-if="item.time==0" :key="item.id" :style="[{ backgroundImage:'url(../../static/pic' + item.img + '.png)'}]"
					 @longpress="clockIn(item.id,item.title,item.today)">
					 </view>
				</view>
			</view>
			<!-- 午间 -->
			<text class="text-black text-xl text-bold">午间</text>
			<view class="bg-white padding">
				<view class="grid col-5 grid-square">
					<view class="bg-img" v-for="item in keepData" v-if="item.time==1" :key="item.id" :style="[{ backgroundImage:'url(../../static/pic' + item.img + '.png)' } ]"
					 @longpress="clockIn(item.id,item.title,item.today)">
					</view>
				</view>
			</view>
			<!-- 晚间 -->
			<text class="text-black text-xl text-bold">晚间</text>
			<view class="bg-white padding">
				<view class="grid col-5 grid-square">
					<view class="bg-img" v-for="item in keepData" v-if="item.time==2" :key="item.id" :style="[{ backgroundImage:'url(../../static/pic' + item.img + '.png)' }]"
					 @longpress="clockIn(item.id,item.title,item.today)">
					</view>
				</view>
			</view>
		</view>
		<!-- 展示习惯打卡列表ed -->
		
		<!-- 新建st -->
		<view class="flex padding justify-end">
			<button class="cu-btn lg light bg-orange shadow-lg round" @tap="createClockin">
				<text class="cuIcon-add"></text> 
			</button>
		</view>
		<!-- 新建ed -->
		
		<!-- 长按后的弹框st -->
		<view class="cu-modal" :class="modalName=='DialogModal'?'show':''">
			<view class="cu-dialog">
				<!-- header -->
				<view class="cu-bar bg-white justify-end">
					<view class="content text-red">我的Flag</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<!-- body -->
				<view class="padding-xl">
					{{note}}
				</view>
				<!-- footer -->
				<view class="cu-bar bg-white col-2">
					<view class="padding">
						<button class="cu-btn bg-green shadow-blur" @tap="comfirm" v-if="todayClockIn == 0">确认打卡</button>
						<button class="cu-btn bg-green shadow-lg" @tap="hideModal" v-if="todayClockIn == 1">已打卡</button>
					</view>
					<view class="padding">
						<button class="cu-btn bg-red margin-left" @tap="del">放弃任务</button>
					</view>
				</view>
			</view>
		</view>
		<!-- 长按后的弹框ed -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				modalName: null,
				keepData:[],
				note: "",
				uid:-1,
				selectedId:-1,
				todayClockIn:0
			}
		},
		methods: {
			showModal: function(e) {
				this.modalName = "DialogModal";
			},

			hideModal: function(e) {
				this.modalName = null
			},

			clockIn: function(eventId, eventTitle, today) {
				this.showModal();
				this.note = '【' + eventTitle + '】';
				if (today == 1){
					this.todayClockIn = 1;
				}
				else{
					this.todayClockIn = 0;
				}
				this.selectedId = eventId;
			},
			
			//获取用户id
			initKeep: function() {
				const _this = this;
				//获取id
				wx.login({
					success(res) {
						if (res.code) {
							//发起网络请求
							const secret = res.code;
							let uinfo = {};
							wx.getUserInfo({
								success: function(res) {
									uinfo = res.userInfo;
									uni.request({
										method: "GET",
										url: "http://localhost:8080/api/UserManage",
										header: {
											"content-type": "application/json"
										},
										data: {
											code: secret,
											gender: uinfo.gender,
											nickname: uinfo.nickName,
											avaUrl: uinfo.avatarUrl
										},
										success: (res) => {
											// console.log(res);
											_this.uid = res.data.uid;
											_this.getHabits();
										},
										fail: (res) => {
											// console.log(res);
											console.log('登录失败!');
										}
									})
								}
							});
						} else {
							console.log('登录失败！' + res.errMsg)
						}
					}
				});

			},

			//获取bigdays
			getHabits: function() {
				//根据id获取清单
				// console.log(this.uid);
				const _this = this;
				uni.request({
					method: 'GET',
					url: 'http://localhost:8080/api/habit/view',
					data: {
						uid: this.uid,
					},
					success: (res) => {
						if (res.data.err == 0) {
							let result = res.data.data;
							for (let i = 0; i < result.length; i++) {
								// console.log(result[i]);
								_this.keepData.push({
									id: result[i].id,
									title: result[i].title,
									img: result[i].img,
									time: result[i].time,
									today: result[i].today_clockin
								});
							}
						} else {
							console.log(res);
							console.log("Bad request.");
						}

					},
					fail: () => {
						console.log("Request failed.");
					}
				})
			},
			
			comfirm:function(){
				const _this = this;
				uni.request({
					method:"GET",
					url:"http://localhost:8080/api/habit/clockin",
					data:{
						uid:_this.uid,
						id:_this.selectedId
					},
					success: (res) => {
						console.log(res);
						_this.hideModal();
					},
					fail: () => {
						console.log("Bad request");
					}
				})
			},
			
			createClockin:function(){
					uni.navigateTo({
						animationType: "slide-in-right",
						url: "./createKeep"
					})
			},
		},
		mounted: function() {
			this.$nextTick(function() {
				this.initKeep();
			})
		},
		onPullDownRefresh() {
			//监听下拉刷新动作的执行方法，每次手动下拉刷新都会执行一次
			this.keepData = [];
			this.initKeep();
		}
	}
</script>

<style>
	@import "../../colorui/animation.css";
</style>
