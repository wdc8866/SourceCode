registerNS('YQJR');
YQJR.jqGridProvider = {
    formatterAmount: function (cellValue, options, rowObject) {
        if (cellValue != undefined) {
            var value = YQJR.AmountWords.thousandValueShow(cellValue);
            var words = YQJR.AmountWords.transferSingle(cellValue);
            var r = '<span title="' + words + '" style="font-size: 17px;font-weight: 600;color:#1B6AAA">' + value + '</span>';
            return r;
        }
        else {
            return ""
        }
    },
    unFormatterAmount: function (cellValue, options) {
        return cellValue;
    },
    formatterCurrency: function (cellValue, options, rowObject) {
        try {
            if (cellValue != '') {
                var text = '';
                var ts = options['colModel']['editoptions']['value'];
                if (ts != undefined) {
                    var texts = ts.split(';');
                    for (var i = 0; i < texts.length; i++) {
                        if (texts[i] != undefined) {
                            var textsInsides = texts[i].split(':');
                            if (textsInsides != undefined) {
                                if (textsInsides[0] == cellValue) {
                                    text = textsInsides[1];
                                    break;
                                }
                            }
                        }
                    }
                    if (cellValue == 'CNY') {
                        return '<span>' + text + '</span>';
                    }
                    else {
                        return '<span style="font-size: 13px;font-weight: 600;color:#d15b47">' + text + '</span>';
                    }
                }
            }
        }
        catch (e) {
            console.log(e);
            return '';
        }
    },
    unFormatterCurrency: function (cellValue, options) {
        return cellValue;
    },
    formatterAccount: function (cellValue, options, rowObject) {
        var title = cellValue;
        if (cellValue != undefined) {
            if (cellValue.length >= 13) {
                title = cellValue.substr(0, 2) + "-" + cellValue.substr(2, 2) + "-" + cellValue.substr(4, 7) + "-" + cellValue.substr(11, cellValue.length - 11);
            }
            return '<span title="' + title + '">' + cellValue + '</span>';
        }
        else {
            return ""
        }
    },
    unFormatterAccount: function (cellValue, options) {
        return cellValue;
    },
    formatterDate: function (cellValue, options, rowObject) {
        if (cellValue != undefined && cellValue != '') {
            var value = '';
            var fromNow = '';
            var yyyymmdd = /^(\d{4}\d{2}\d{2})/;
            try {
                if (yyyymmdd.test(cellValue)) {
                    value = moment(cellValue, 'YYYYMMDD').format('YYYY-MM-DD');
                    fromNow = moment(cellValue, 'YYYYMMDD').fromNow();
                }
                else {
                    value = moment(cellValue).format('YYYY-MM-DD');
                    fromNow = moment(cellValue).fromNow();
                }
            } catch (e) {
                console.log(e);
                return "";
            }
            var r = '<span title="' + fromNow + '">' + value + '</span>';
            return r;
        }
        else {
            return "";
        }
    },
    unFormatterDate: function (cellValue, options) {
        return cellValue;
    },
    HalfScreenRemoveScroll: function (tableId) {
        var table = $('#' + tableId);
        if (table != undefined) {
            for (var i = 0; i < 5; i++) {
                table = table.parent();
                if (table.prop('tagName') == 'DIV') {
                    if (table.css('overflow-x') == 'scroll') {
                        table.css('overflow-x', 'hidden');
                    }
                    break;
                }
            }
        }
    },
    formatterSplit:function(cellValue,options)
    {
        var values=cellValue.split(',');
        console.log(values);
        var result="";
        $.each(values, function (index, value) {
           console.log(value);
            result+='<span class="label label-success middle" style="margin-right: 1px">'+value+'</span>'
        });
        return result;
    },
    overLimitColor: function (cellValue, options, rowObject) {
        if (cellValue != undefined && cellValue != '') {
            var text = '';
            var ts = options['colModel']['editoptions']['value'];
            if (ts != undefined) {
                var texts = ts.split(';');
                for (var i = 0; i < texts.length; i++) {
                    if (texts[i] != undefined) {
                        var textsInsides = texts[i].split(':');
                        if (textsInsides != undefined) {
                            if (textsInsides[0] == cellValue) {
                                text = textsInsides[1];
                                break;
                            }
                        }
                    }
                }
                if (cellValue == 'Y') {
                    return '<span style="font-size: 13px;font-weight: 600;color:red">' + text + '</span>';
                }
                else {
                    return '<span>' + text + '</span>';
                }
            }
        }
        else {
            return ""
        }
    },
    overLimitGroupNameColor: function (cellValue, options, rowObject) {
        if (cellValue != undefined && cellValue != '') {
            console.log(rowObject);
            var flg = rowObject['overFlg'];
            if (flg == 'Y') {
                return '<span style="font-size: 13px;font-weight: 600;color:red">' + cellValue + '</span>';
            }
            else {
                return '<span>' + cellValue + '</span>';
            }
        }
        else {
            return ""
        }
    },
    showRowData: function (data) {
        if (data != undefined) {
            var containerHtml = '<div id="jqGridRowDataShow" class="hide"><div id="jqGridRowDataShowContainer" class="container-fluid"></div></div>';
            var container = $(containerHtml).find('#jqGridRowDataShowContainer');
            //var container=$('#jqGridRowDataShowContainer');
            container.html(initDataShow(data));
            var dialog = container.removeClass('hide').dialog({
                resizable: true,
                width: '750',
                height: '450',
                closeText: '',
                open: function () {
                    setHash();
                },
                modal: false,
                title: "<div class='widget-header widget-header-small widget-header-small-blue'><h4 class='smaller white'>详细数据</h4></div>",
                title_html: true,
                position: {
                    using: function (pos) {
                        console.log(pos);
                        var topOffset = $(this).css(pos).offset().top;
                        if (topOffset < 0) {
                            $(this).css("top", pos.top - topOffset);
                        }
                        else
                        {
                            $( this ).css( "top", pos.top-100 );
                        }
                    }
                },
                buttons: [
                    {
                        text: "关闭",
                        "class": "btn btn-minier",
                        click: function () {
                            dialog.dialog("close");
                        }
                    }]
            });

            function initDataShow(data) {
                var templateContainer1 = '<div class="profile-user-info profile-user-info-striped">{1}</div>';
                var templateContainer2 = '<div class="profile-info-row">{2}</div>';
                var templateName = '<div class="profile-info-name">{name}</div>';
                var templateValue = '<div class="profile-info-value"><span class="editable">{content}</span></div>';

                var container1 = '';
                var container2 = '';
                var info = '';
                for (var i = 0, j = 0; i <= data.length; i++, j++) {
                    var row = data[i];
                    while (true) {
                        if (row != undefined) {
                            if (row['hidden'] != true) {
                                break;
                            }
                            else {
                                i++;
                                row = data[i];
                                continue;
                            }
                        }
                        else {
                            break;
                        }
                    }
                    if (row != undefined) {
                        var name = templateName.replace('{name}', row['name']);
                        var content = templateValue.replace('{content}', row['content']);
                        if (j % 2 != 0) {
                            info = info + name + content;
                            container2 = container2 + templateContainer2.replace('{2}', info);
                            info = '';
                        }
                        else {
                            info = name + content;
                        }
                    }
                }
                if (info != '') {
                    container2 = container2 + templateContainer2.replace('{2}', info);
                }
                container1 = templateContainer1.replace('{1}', container2);

                return container1;
            }
        }
    }
}