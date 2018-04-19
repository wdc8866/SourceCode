YQJR.AmountWords = {
    transfer: function (n) {
        var fraction = ['角', '分'];
        var digit = [
            '零', '壹', '贰', '叁', '肆',
            '伍', '陆', '柒', '捌', '玖'
        ];
        var unit = [
            ['元', '万', '亿'],
            ['', '拾', '佰', '仟']
        ];
        var head = n < 0 ? '负' : '';
        n = Math.abs(n);

        var s = '';

        for (var i = 0; i <fraction.length; i++) {
            //s += (digit[Math.floor(n * 10 * Math.pow(10, i)+0.001) % 10] + fraction[i]).replace(/零./, '');
            s += (digit[Math.floor(n * 10 * Math.pow(10, i)+0.001) % 10] + fraction[i]);
        }
        if(s=='零角零分')
        {
            s='';
        }
        s = s || '整';
        n = Math.floor(n);

        for (var i = 0; i < unit[0].length && n > 0; i++) {
            var p = '';
            for (var j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[n % 10] + unit[1][j] + p;
                n = Math.floor(n / 10);
            }
            s = p.replace(/(零.)*零$/, '')
                    .replace(/^$/, '零')
                + unit[0][i] + s;
        }
        return head + s.replace(/(零.)*零元/, '元')
                .replace(/(零.)+/g, '零')
                .replace(/^整$/, '零元整')
                .replace(/零$/,'');
    },
    transferSingle: function (n) {
        if (n != undefined && n != '') {
            var fraction = ['角', '分'];
            var digit = [
                '零', '壹', '贰', '叁', '肆',
                '伍', '陆', '柒', '捌', '玖'
            ];
            var unit = [
                ['元', '万', '亿'],
                ['', '拾', '佰', '仟']
            ];
            var head = n < 0 ? '负' : '';
            n = Math.abs(n);

            var s = '';

            for (var i = 0; i < fraction.length; i++) {
                //s += (digit[Math.floor(n * 10 * Math.pow(10, i)+0.001) % 10] + fraction[i]).replace(/零./, '');
                s += (digit[Math.floor(n * 10 * Math.pow(10, i)+0.001) % 10] + fraction[i]);
            }
            if(s=='零角零分')
            {
                s='';
            }
            s = s || '整';
            n = Math.floor(n);

            for (var i = 0; i < unit[0].length && n > 0; i++) {
                var p = '';
                for (var j = 0; j < unit[1].length && n > 0; j++) {
                    p = digit[n % 10] + unit[1][j] + p;
                    n = Math.floor(n / 10);
                }
                s = p.replace(/(零.)*零$/, '')
                        .replace(/^$/, '零')
                    + unit[0][i] + s;
            }
            return head + s.replace(/(零.)*零元/, '元')
                    .replace(/(零.)+/g, '零')
                    .replace(/^整$/, '零元整')
                    .replace(/零$/,'');
        }
        return '无';
    },
    show: function (amountTag) {
        if (amountTag != undefined) {
            amountTag.css("font-weight", "600");
            amountTag.css("font-size", "17px");
            amountTag.css("color", "#1B6AAA");
            var check = /(^-?[0-9]{1,12}(\.)[0-9]{1,2}$)|(^-?[0-9]{1,12}$)/;
            var id = "YQJR_BigNumber" + amountTag.attr("id");
            var before = "";
            if (check.test(amountTag.val().replace(/\$|\,/g, ''))) {
                $('#' + id).remove();
                if (Number(amountTag.val().replace(/\$|\,/g, '')) != 0) {
                    var words = YQJR.AmountWords.transfer(amountTag.val().replace(/\$|\,/g, ''));
                    //var content = '<div id="' + id + '" class="widget-box transparent"><div class="widget-body"><div class="widget-main no-padding"></div><h5 style="color: #1c29a6">' + words + '</h5></div></div>';
                    var content = '<label id="' + id + '" style="color: #1c29a6">' + words + '</label>';
                    amountTag.after(content);
                    setHash();
                    amountTag.val(formatCurrencyFull(amountTag.val().replace(/\$|\,/g, '')));
                }
            }

            amountTag.focus(function () {
                amountTag.select();
            });
            amountTag.keydown(function () {
                before = amountTag.val().replace(/\$|\,/g, '');
            });
            amountTag.blur(function () {
                amountTag.val(formatCurrencyFull(amountTag.val().replace(/\$|\,/g, '')));
            });
            amountTag.bind("propertychange input", function () {
                var now = amountTag.val().replace(/\$|\,/g, '');
                var checkPoint = /^-?([0-9]{1,12}(\.))?$/;
                if (check.test(now) || amountTag.val() == "" || checkPoint.test(now)) {
                    now = amountTag.val().replace(/\$|\,/g, '');
                }
                else {
                    now = before;
                }
                if (String(now) != '-0' && String(now) != '-0.' && String(now) != '-0.0'&& String(now) != '-0.00') {
                    amountTag.val(formatCurrency(now));
                }
                else {
                    amountTag.val(now);
                }
                $('#' + id).remove();
                if (now != "") {
                    if (now != '-' && Number(now) != 0) {
                        var words = YQJR.AmountWords.transfer(now);
                        if (words != undefined) {
                            //var content = '<div id="' + id + '" class="widget-box transparent"><div class="widget-body"><div class="widget-main no-padding"></div><h5 style="color: #1c29a6">' + words + '</h5></div></div>';
                            var content = '<label id="' + id + '" style="color: #1c29a6">' + words + '</label>';
                            amountTag.after(content);
                            setHash();
                        }
                    }
                }
            });
        }
    },
    labelShow: function (amountTag) {
        if (amountTag != undefined) {
            amountTag.css("font-weight", "600");
            amountTag.css("font-size", "17px");
            amountTag.css("color", "#1B6AAA");
            var id = "IBMP_BigNumber" + amountTag.attr('id');
            $('#' + id).remove();
            if (Number(amountTag.val().replace(/\$|\,/g, '')) != 0) {
                var words = YQJR.AmountWords.transfer(amountTag.val().replace(/\$|\,/g, ''));
                if (words != undefined) {
                    //var content = '<div id="' + id + '" class="widget-box transparent"><div class="widget-body"><div class="widget-main no-padding"></div><h5 style="color: #1c29a6">' + words + '</h5></div></div>';
                    var content = '<label id="' + id + '" style="color: #1c29a6">' + words + '</label>';
                    amountTag.after(content);
                    setHash();
                }
                var tvalue = parseFloat(amountTag.val().replace(/\$|\,/g, ''));
                if (tvalue == 0) {
                    amountTag.val('0.00');
                }
                else {
                    amountTag.val(formatCurrencyFull(amountTag.val().replace(/\$|\,/g, '')));
                }
            }
        }
    },
    amountWordsCancel: function (amountTag) {
        if (amountTag != undefined) {
            var id = "IBMP_BigNumber" + amountTag.attr('id');
            $('#' + id).remove();
        }
    },
    thousandShow: function (amountTag) {
        if (amountTag != undefined) {
            amountTag.val(formatCurrencyFull(amountTag.val()));
        }
    },
    thousandValueShow: function (value) {
        if (value != undefined && value != '') {
            return formatCurrencyFull(value);
        }
        return 0.00;
    }

};

/**
 * 将数值四舍五入(保留2位小数)后格式化成金额形式
 *
 * @param num 数值(Number或者String)
 * @return 金额格式的字符串,如'1,234,567.45'
 * @type String
 * @author yaohy
 */
function formatCurrency(num) {
    num = num.toString().replace(/\$|\,/g, '');
    var point = 1;
    var cents;
    if (/^(0|-?[1-9]\d*)(\.)$/.test(num)) {
        point = 2;
    }
    else if (/^(0|-?[1-9]\d*)(\.\d+)?|-0\.\d+$/.test(num)) {
        cents = num.split('.')[1];
    }
    else if (num == '-') {
        return num;
    }
    else {
        return "";
    }

    if (point == 2) {
        num = num.replace('.', '');
    }
    var sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    num = Math.floor(num / 100).toString();
    var c = "";
    if (point == 2) {
        c = ".";
    }
    else if (point == 1) {
        if (cents != undefined) {
            c = "." + cents;
        }
    }

    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) {
        num = num.substring(0, num.length - (4 * i + 3)) + ',' +
            num.substring(num.length - (4 * i + 3));
    }
    return (((sign) ? '' : '-') + num) + c;
}

function formatCurrencyFull(num) {
    if (!/^((0|-)?[0-9]\d*)(\.\d+)?$/.test(num))
        return "";
    num = num.toString().replace(/\$|\,/g, '');
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    cents = num % 100;
    num = Math.floor(num / 100).toString();
    if (cents < 10) {
        cents = "0" + cents;
    }
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) {
        num = num.substring(0, num.length - (4 * i + 3)) + ',' +
            num.substring(num.length - (4 * i + 3));
    }
    return (((sign) ? '' : '-') + num + '.' + cents);
}



