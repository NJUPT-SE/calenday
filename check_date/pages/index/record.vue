<template>
	<view class="main_part">
		<view class="show_habit" v-for="iterm in habit_object">
			<image :src='iterm.img' mode="aspectFit"></image>
			<text>{{iterm.title}}</text>
			<text>{{'打卡次数'+iterm.number}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				user_id: 1,
				//habit_object:[{}],
				//测试用例
				habit_object: [{
						"id": 1,
						"title": "打卡内容A",
						"img": "1",
						"number": "12",
					},
					{
						"id": 2,
						"title": "打卡内容B",
						"img": "2",
						"number": "15",
					},
				]
			}
		},

		onLoad: function(e) {
			uni.request({
				url: 'http://localhost/api/habit/view',
				data: {
					user_id: this.user_id,
				},
				success: (res) => {
					/*错误接收暂且不管*/
					// err = res.err,
					this.habit_object = res
				}
			})
		},
		methods: {

		}
	}
</script>

<style>
.crad_image {
		width: 100rpx;
		height: 100rpx;
		padding: 10rpx;
	}
	.show_habit{
		
	}
.main_part{
	flex-direction: column;
	flex-wrap: wrap;
	justify-content: space-around;
	align-content: flex-start;
	flex: auto |[ <'flex-grow'> <'flex-shrink'>? || <'flex-basis'>]
}
</style>
