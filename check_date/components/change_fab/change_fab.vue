<template>
	<!--悬浮按钮-->
	<view>
		<uni-fab 
		:pattern="pattern" 
		:content="content" 
		:horizontal="horizontal" 
		:vertical="vertical" 
		:direction="direction"
		
		 @trigger="trigger"
		 ></uni-fab>
	</view>
</template>

<script>
	import uniFab from '@/components/uni-fab/uni-fab.vue';
	import uniIcon from '@/components/uni-icons/uni-icons.vue';
	
	export default {
		/*悬浮按钮数据段*/
		components: {
			uniFab,
			uniIcon
		},

		data() {
			return {
				pattern: {
					color: '#7A7E83',
					backgroundColor: '#FFFFFF',
					selectedColor: '#db824d',
					buttonColor: '#e26731'
				},
				content: [{
					iconPath: '../../static/card_icons/增加.png',
					selectedIconPath: '../../static/card_icons/增加cl.png',
					text: "新建",
					active: false
				}, {
					iconPath: '../../static/card_icons/编辑.png',
					selectedIconPath: '../../static/card_icons/编辑cl.png',
					text: "编辑",
					active: false
				}, {
					iconPath: '../../static/card_icons/删除.png',
					selectedIconPath: '../../static/card_icons/删除cl.png',
					text: "删除",
					active: false
				}, {
					iconPath: '../../static/card_icons/记录.png',
					selectedIconPath: '../../static/card_icons/记录cl.png',
					text: "记录",
					active: false
				}],
				horizontal: 'right',
				vertical: 'bottom',
				direction: 'vertical',
			}
		},
		methods: {
			/*悬浮按钮响应*/
			trigger(e) {
				this.content[e.index].active = !e.item.active;
				if (this.content[e.index].text == "新建") {
					console.log("新建被点击"),
					uni.navigateTo({
						url: '../../pages/index/creat_card?user_id'+user_id
					})
				} else if (this.content[e.index].text == "编辑") {
					console.log("编辑被点击")
					uni.navigateTo({
						url:'../../pages/index/edit',
					})
				} else if (this.content[e.index].text == "删除") {
					console.log("删除被点击")
					uni.navigateTo({
						url:'../../pages/index/delet',
					})
				}else if (this.content[e.index].text == "记录"){
					console.log("记录被点击")
					uni.navigateTo({
						url:'../../pages/index/record',
					})
				}
			},
		}
	}
</script>

<style>

</style>
