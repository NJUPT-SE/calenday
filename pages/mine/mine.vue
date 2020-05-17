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
				<view class="padding bg-white solid">
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
				userName: "赖小七",
				avaUrl: "background-image:url",
				continuity: 3,
				total: 5,
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

		},
		onLoad() {
			this.getUser();
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
