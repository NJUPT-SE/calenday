<template>
	<view>
		<view class="cradius shadow bg-white">
			<!-- 用户信息st -->
			<view class="grid flex padding col-2">
				<!-- 基本信息 -->
				<view class="flex align-center">
					<!-- 头像 -->
					<view class="padding-right">
						<view class="cu-avatar xl round" :style="avaUrl"></view>
					</view>
					<view class=" text-xl">
						<text class="text-black text-bold">{{userName}}</text>
					</view>
				</view>

				<!-- 注销 -->
				<view class="flex justify-end" @tap="showModal" data-target="DialogModal">
					<view class="padding-top text-xxl">
						<text class="cuIcon-right text-red"></text>
					</view>
				</view>


			</view>
			<!-- 用户信息ed -->

			<!-- 三栏小图标st -->
			<view class="grid margin-bottom text-center col-3">
				<!-- 第一栏st -->
				<view class="padding bg-white solid">
					<view class="flex-sub text-center">
						<!-- 天数 -->
						<view class="text-xl">
							<text class="text-black text-bold">{{continuity}}天</text>
						</view>
						<!-- 打卡文字 -->
						<view class="text-sm">
							<text class="text-grey">已连续坚持</text>
						</view>
					</view>
				</view>
				<!-- 第一栏ed -->

				<!-- 第二栏st -->
				<view class="padding bg-white solid" @tap="achievements">
					<view class="flex-sub text-center">
						<!-- 次数 -->
						<view class="text-xl">
							<text class="text-black text-bold">{{total}}次</text>
						</view>
						<!-- 打卡文字 -->
						<view class="text-sm">
							<text class="text-grey">总打卡数</text>
						</view>
					</view>
				</view>
				<!-- 第二栏ed -->

				<!-- 第三栏st -->
				<view class="padding bg-white solid">
					<view class="flex-sub text-center">
						<!-- 打卡图标 -->
						<view class="text-xxl">
							<text class="cuIcon-roundcheckfill text-green" v-if="today" @tap="today = !today"></text>
							<text class="cuIcon-countdown text-red" v-if="!today" @tap="today = !today"></text>
						</view>
						<!-- 打卡文字 -->
						<view class="text-sm">
							<text class="text-grey">今日打卡</text>
						</view>
					</view>
				</view>
				<!-- 第三栏ed -->
			</view>
			<!-- 三栏小图标ed -->


		</view>
		<!-- 注销弹框 -->
		<view class="cu-modal" :class="modalName=='DialogModal'?'show':''">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content text-red">提醒</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<view class="padding-xl">
					您确定退出账号？
				</view>
				<view class="cu-bar bg-white justify-end">
					<view class="action">
						<button class="cu-btn line-green text-green" @tap="hideModal">取消</button>
						<button class="cu-btn bg-green margin-left" @tap="logout">确定</button>

					</view>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				uid:-1,
				userName: "赖小七",
				avaUrl: "background-image:url",
				continuity: 0,
				total: 0,
				today: true,
				modalName: null,
			}
		},
		methods: {
			getUser: function() {
				let _this = this;
				uni.getSetting({
					success(res) {
						if (res.authSetting['scope.userInfo']) {
							// 已经授权，可以直接调用 getUserInfo 获取头像昵称
							wx.getUserInfo({
								success: function(res) {
									_this.userName = res.userInfo.nickName;
									_this.avaUrl += "(" + res.userInfo.avatarUrl + ");";
									console.log(_this.userName, _this.avaUrl);
								}
							})
						} else {
							console.log("failed");
						}
					}
				})
			},

			showModal: function(e) {
				this.modalName = e.currentTarget.dataset.target
			},

			hideModal: function(e) {
				this.modalName = null
			},

			logout: function() {
				this.hideModal();
				uni.navigateTo({
					url: "../index/index"
				})
			},
			
			achievements:function(){
					uni.navigateTo({
						url:"../keep/achievement",
					})
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
											_this.uid = res.data.uid;
											_this.getHabits();
											// console.log(_this.uid);
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
						uid: _this.uid,
					},
					success: (res) => {
						if (res.data.err == 0) {
							console.log(res);
							let result = res.data.data;
							for (let i = 0; i < result.length; i++) {
								_this.continuity = Math.max(_this.continuity, result[i].continue_clockin);
								_this.total = Math.max(_this.total, result[i].total_clockin);
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
		},
		onLoad() {
			this.getUser();
			this.initKeep();
		}
	}
</script>

<style>
	.user-name {
		margin-left: 5%;
	}


	.avatar {
		border-radius: 100%;
		height: 150rpx;
		width: 150rpx;
	}
</style>
