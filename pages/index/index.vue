<template>
	<view class="padding bg-white bg-full">
		<!-- 图标st -->
		<view class="padding center">
			<image src="@/static/calenday.png" class="logo"></image>
		</view>
		<!-- 图标ed -->

		<!-- 登录按钮st -->
		<view class="padding flex flex-direction">
			<button class="cu-btn bg-green lg shadow" @tap="showModal" data-target="bottomModal">微信登录
			</button>
		</view>
		<!-- 登录按钮ed -->

		<!-- 弹出框st -->
		<view class="cu-modal bottom-modal" :class="modalName=='bottomModal'?'show':''">
			<view class="cu-dialog">
				<!-- 按钮选项 -->
				<view class="cu-bar bg-white">
					<view class="action text-red" @tap="hideModal">拒绝</view>
					<view class="action text-green" @tap="login">登录</view>
				</view>

				<!-- 内容 -->
				<view class="padding-xl bg-white">
					<!-- 头部 -->
					<view class="flex-sub text-left">
						<view class="text-xl">
							<text class="text-black text-bold">点滴申请</text>
						</view>
					</view>
					<!-- 信息 -->
					<view class="flex-sub text-center">
						<view class="text-xl padding">
							<text class="text-black">想获取您的微信昵称、头像等信息</text>
						</view>
					</view>
					<!-- 按钮 -->
					<button class="bg-green" wx:if="canIUse" open-type="getUserInfo">授权登录</button>
					<view wx:else>请升级微信版本</view>
				</view>

			</view>
		</view>
		<!-- 弹出框ed -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				canIUse: wx.canIUse('button.open-type.getUserInfo'),
				modalName: null,
			}
		},
		methods: {
			login: function() {
				wx.getSetting({
					success(res) {
						if (res.authSetting['scope.userInfo']) {
							// 已经授权，可以直接调用 getUserInfo 获取头像昵称
							wx.login({
								success(res) {
									if (res.code) {
										//发起网络请求
										const secret = res.code;
										console.log(res.code);
										let uinfo = {};
										wx.getUserInfo({
											success: function(res) {
												uinfo = res.userInfo;
												uni.request({
													method:"GET",
													url:"http://localhost:8080/api/UserManage",
													header:{
														"content-type":"application/json"
													},
													data:{
														code:secret,
														gender:uinfo.gender,
														nickname:uinfo.nickName,
														avaUrl:uinfo.avatarUrl
													},
													success: (res) => {
														console.log(res);
														uni.switchTab({
															url:"../mine/mine"
														})
													},
													fail: (res) => {
														console.log(res);
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
						} else {
							console.log("请先授权！");
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
		},
	}
</script>

<style>
	.logo {
		height: 600rpx;
		width: 100%;
	}

	.bg-full {
		padding-bottom: 200%;
	}
</style>
