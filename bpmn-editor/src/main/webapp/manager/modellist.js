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

                    ModelList._addImage(this, row);

                    var container = $("<div class='model-detail'></div>").appendTo(row);
                    ModelList._addTitle(this, container);
                    ModelList._addDescription(this, container);
                    ModelList._addActions(this, container);

                    models.append(row);
                });

                $('.dropdown-toggle').dropdown();
            });
        },

        _addImage : function(model, container) {
            container.append("<a class='pull-left' href='"+model.links.editor+"'><img class='media-object model-img' src='"+model.url+"/svg'/></a>");
        },

        _addTitle : function(model, container) {
            var title = $("<h4 class='media-heading'></h4>").appendTo(container);
            if (typeof model.links.editor != "undefined" && model.links.editor != null)
                title.append("<a target='_blank' href='"+model.links.editor+"'>"+model.name+"</a>");
            else
                title.append(model.name);

            if (! model.owner) {
                title.append(" <small>(Shared model)</small>")
            }
        },

        _addDescription : function(model, container) {
            if (typeof model.description != "undefined" && model.description != null)
                container.append("<div>"+model.description+"</div>");
        },

        _addActions : function(model, container) {
            var actions = $("<ul class='actions inline'></ul>").appendTo(container);

            ModelList._addExport(model, actions);
            ModelList._addLinks(model, actions);
            ModelList._addHandling(model, actions);
        },

        _addExport : function(model, container) {
            var exportmenu = $('<ul class="dropdown-menu" role="menu"></ul>');
            var added = 0;
            for (key in model.export) {
                exportmenu.append("<li><a tabindex='-1' target='_blank' href='"+model.export[key]+"'>"+key+"</a></li>");
                added++;
            }

            if (added > 0) {
                var exportAction = $('<li class="dropdown"></li>');
                $('<a class="btn btn-mini btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><i class="icon-download icon-white"></i> Download As... <span class="caret"></span></a>').prependTo(exportAction);
                exportAction.append(exportmenu);
                container.append(exportAction);
            }
        },

        _addLinks : function(model, container) {
            for (key in model.links) {
                if (key != "editor")
                    container.append("<li><a class='btn btn-mini btn-primary' href='"+model.links[key]+"'>" + key + "</a></li>");
            }
        },

        _addHandling : function(model, container) {
            if (NavBar.isLogged()) {
                var remove = $("<a class='btn btn-mini btn-primary' href='#'><i class='icon-trash icon-white'></i> Delete model</a>").click(function() {
                    removeModelDialog(model);
                });
                $("<li></li>").append(remove).appendTo(container);

                var clone = $("<a class='btn btn-mini btn-primary' href='#'><i class='icon-th-large icon-white'></i> Clone model</a>").click(function() {
                    openAddDialog("Clone " + model.name, {cloneFrom: model.modelId, type: model.type });
                });
                $("<li></li>").append(clone).appendTo(container);

                if (model.owner) {
                    var share = $("<a class='btn btn-mini btn-primary' href='#'><i class='icon-share-alt icon-white'></i> Share model</a>").click(function() {
                        openShareDialog(model);
                    });
                    $("<li></li>").append(share).appendTo(container);
                }
            }
        }
    };
})(jQuery);