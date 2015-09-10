jQuery(function($) {
	/* 时间选择 */
	$('input[id=date-range-picker]').daterangepicker().prev().on(
			ace.click_event, function() {
				$(this).next().focus();
			});

});

function page(action) {
	document.list_form.action = action;
	list_form.submit();
}