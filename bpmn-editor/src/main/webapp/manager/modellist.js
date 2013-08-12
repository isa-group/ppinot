if (! MODELHANDLER) {
    var MODELHANDLER = new ModelHandler();
}

var ModelList = {};

(function($) {
    ModelList = {
        loadModels : function(models) {
            MODELHANDLER.loadModelsList().done(function(processes) {
                models.empty();

                $(processes).each(function() {
                    var row=$('<li class="clearfix model-row"></li>');

                    row.append("<a class='pull-left' href='"+this.editor+"'><img class='media-object model-img' src='"+this.url+"/svg'/></a>");

                    var container = $("<div class='model-detail'></div>").appendTo(row);

                    ModelList._addTitle(this, container);

                    if (typeof this.description != "undefined" && this.description != null)
                        container.append("<div>"+this.description+"</div>");

                    var actions = $("<ul class='actions inline'></ul>").appendTo(container);
                    ModelList._addActions(this, actions);

                    models.append(row);
                });

                $('.dropdown-toggle').dropdown();
            });
        },

        _addTitle : function(model, container) {
            var title = $("<h4 class='media-heading'></h4>").appendTo(container);
            if (typeof model.editor != "undefined" && model.editor != null)
                title.append("<a target='_blank' href='"+model.editor+"'>"+model.name+"</a>");
            else
                title.append(model.name);
        },

        _addActions : function(model, container) {
            container.append("<li><a class='btn btn-mini btn-primary' href='"+model.url+"'><i class='icon-download icon-white'></i> Download XML</a></li>");
            var exportAction = $('<li class="dropdown"></li>');
            var exportmenu = $('<ul class="dropdown-menu" role="menu"></ul>');
            exportmenu.append("<li><a tabindex='-1' target='_blank' href='"+model.url+"/pdf'>PDF</a></li>");
            exportmenu.append("<li><a tabindex='-1' target='_blank' href='"+model.url+"/png'>PNG</a></li>");
            $('<a class="btn btn-mini btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-share icon-white"></i> Export to...</a>').prependTo(exportAction);
            exportAction.append(exportmenu);

            container.append(exportAction);

            container.append("<li><a class='btn btn-mini btn-primary' href='ppi-template.html#/#"+model.modelId+"'>View PPIs</a></li>");


            if (NavBar.isLogged()) {
                var remove = $("<a class='btn btn-mini btn-primary' href='#'><i class='icon-trash icon-white'></i> Delete model</a>").click(function() {
                    $("#removeModelId").html(model.modelId);
                    $("#acceptRemove").unbind().click(function() {
                        console.log("Removed "+model.modelId);
                        $("#removeModal").modal("hide");
                        MODELHANDLER.removeModel(model.modelId);
                        loadModels();
                    });
                    $("#removeModal").modal("show");
                });
                $("<li></li>").append(remove).appendTo(container);
            }

        }
    };
})(jQuery);