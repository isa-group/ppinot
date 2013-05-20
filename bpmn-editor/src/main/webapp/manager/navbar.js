if (! MODELHANDLER) {
    var MODELHANDLER = new ModelHandler();
}

var NavBar = {};

(function($) {
    NavBar = {
        namesList: [],
        mapNamesId: {},

        modelsSourceLoader: function (q, callback) {
            MODELHANDLER.loadModelsList().done(function(processes) {
                NavBar.namesList = [];
                NavBar.mapNamesId = {};
                $(processes).each(function() {
                    if (typeof this.name != "undefined" && typeof this.editor != "undefined" && this.editor != null) {
                        var name = this.name + " ("+this.modelId+")";
                        NavBar.namesList.push(name);
                        NavBar.mapNamesId[name] = this.editor;
                    }
                });

                callback(NavBar.namesList);
            });
        }

    };
})(jQuery);



jQuery(document).ready(function($) {
    $("#editModelNavbar").typeahead({
        source: NavBar.modelsSourceLoader,
        items: 6
    });

    $("#editModelFormNavbar").submit(function() {
        location.href = NavBar.mapNamesId[$("#editModelNavbar").val()];

        return false;
    });
});