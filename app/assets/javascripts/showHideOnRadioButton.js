// UI module (common code)
(function (UI, $, undefined) {
    UI.show = function (selector) {
        $(selector).removeClass("hidden");
    };

    UI.hide = function (selector) {
        $(selector).addClass("hidden");
    };

    UI.hideShowOnRadioButton = function(radioGroupName, buttonToAreaMap) {
        var updateState = function(buttonMap) {
            for (var b in buttonMap) {
                if ($(b).is(":checked")) {
                    UI.show($(buttonMap[b]));
                } else {
                    UI.hide($(buttonMap[b]));
                }
            }
        };
        // on state change handler
        var radioGroup = $("input[name='"+radioGroupName+"']:radio");
        radioGroup.on("change", function () {
            updateState(buttonToAreaMap);
        }).trigger("change");
    };
}(window.UI = window.UI || {}, jQuery));

// DoYouHaveVatRegNumberPage module
(function (DoYouHaveVATRegNumberPage, $, undefined) {
    DoYouHaveVATRegNumberPage.init = function() {
        UI.hideShowOnRadioButton("doYouHaveVATRegNumber",
            { "#doYouHaveVATRegNumber-true": "#doYouHaveVATRegNumberPanel" });
    }
}(window.DoYouHaveVATRegNumberPage = window.DoYouHaveVATRegNumberPage || {}, jQuery));