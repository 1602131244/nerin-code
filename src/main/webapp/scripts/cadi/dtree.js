/*
var SOURCE = [
		{title: "DD0", folder: true, expanded: true, children: [
			{title: "0101"},
			{title: "0102"}
		]},
		{title: "DD1", folder: true, expanded: false, children: [
			{title: "0201"},
			{title: "0202"}
		]}
	],
*/

$(function(){
	

	/*
	 * Context menu (https://github.com/mar10/jquery-ui-contextmenu)
	 */
	$("#chooseArea").contextmenu({
		delegate: "span.fancytree-node",
		menu: [
			{title: "Edit <kbd>[F2]</kbd>", cmd: "rename", uiIcon: "ui-icon-pencil" },
			{title: "Delete <kbd>[Del]</kbd>", cmd: "remove", uiIcon: "ui-icon-trash" },
			{title: "----"},
			{title: "New sibling <kbd>[Ctrl+N]</kbd>", cmd: "addSibling", uiIcon: "ui-icon-plus" },
			{title: "New child <kbd>[Ctrl+Shift+N]</kbd>", cmd: "addChild", uiIcon: "ui-icon-arrowreturn-1-e" },
			{title: "----"},
			{title: "Cut <kbd>Ctrl+X</kbd>", cmd: "cut", uiIcon: "ui-icon-scissors"},
			{title: "Copy <kbd>Ctrl-C</kbd>", cmd: "copy", uiIcon: "ui-icon-copy"},
			{title: "Paste as child<kbd>Ctrl+V</kbd>", cmd: "paste", uiIcon: "ui-icon-clipboard", disabled: true }
		],
		beforeOpen: function(event, ui) {
			var node = $.ui.fancytree.getNode(ui.target);
			$("#chooseArea").contextmenu("enableEntry", "paste", !!CLIPBOARD);
			node.setActive();
		},
		select: function(event, ui) {
			var that = this;
			// delay the event, so the menu can close and the click event does
			// not interfere with the edit control
			setTimeout(function(){
				$(that).trigger("nodeCommand", {cmd: ui.cmd});
			}, 100);
		}
	});
});