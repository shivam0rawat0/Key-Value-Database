browser.browserAction.onClicked.addListener(function(tab) {
	browser.tabs.query({ active: true, currentWindow: true }, function(tabs) {
		if (tabs.length > 0 && tabs[0].url) {
			localStorage.setItem("myData",tabs[0].url);
		}
	});
	browser.windows.create({
		url: browser.extension.getURL("dataForm.html"),
		type: "popup",
		width: 300,
		height: 100
	});
});