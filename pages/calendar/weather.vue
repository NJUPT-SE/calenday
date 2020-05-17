<template>
	<view>
		<web-view :src="weather" style="height: 50%;"></web-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				weather:"https://widget-page.heweather.net/h5/index.html?bg=1&md=01346&lc=auto&key=179d3137a1fd4dbfb6f971c0ee8a66da",
			}
		},
		methods: {
			
		}
	}
</script>

<style>

</style>
