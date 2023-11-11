/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 82.69, "KoPercent": 17.31};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.6207833333333334, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.66295, 500, 1500, "http://localhost:8080/api/v1/shopping-lists/{id} - DELETE"], "isController": false}, {"data": [0.6682, 500, 1500, "http://localhost:8080/api/v1/shopping-lists?page=0"], "isController": false}, {"data": [0.00935, 500, 1500, "Test"], "isController": true}, {"data": [0.67135, 500, 1500, "http://localhost:8080/api/v1/shopping-lists"], "isController": false}, {"data": [0.66465, 500, 1500, "http://localhost:8080/api/v1/shopping-lists/{id} - GET"], "isController": false}, {"data": [0.66315, 500, 1500, "http://localhost:8080/api/v1/shopping-lists/{id} - PATCH"], "isController": false}, {"data": [0.7095, 500, 1500, "http://localhost:8080/api/v1/products"], "isController": false}, {"data": [0.6897, 500, 1500, "http://localhost:8080/api/v1/auth/access_token"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 110000, 19041, 17.31, 377.47747272727855, 0, 3905, 230.0, 669.9000000000015, 835.0, 1186.0, 510.24431425483453, 515.2724177361504, 361.57068583358847], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["http://localhost:8080/api/v1/shopping-lists/{id} - DELETE", 10000, 1731, 17.31, 385.89779999999945, 0, 3355, 307.0, 864.0, 1084.949999999999, 1550.9899999999998, 46.523745719815395, 15.850921853482768, 32.97556754608177], "isController": false}, {"data": ["http://localhost:8080/api/v1/shopping-lists?page=0", 30000, 5193, 17.31, 381.6057666666667, 0, 3905, 290.0, 828.0, 1008.0, 1444.0, 139.2796456725814, 251.38533828559522, 83.69566896884314], "isController": false}, {"data": ["Test", 10000, 1731, 17.31, 4152.252399999979, 107, 11070, 4611.0, 6406.799999999999, 6934.0, 8131.969999999999, 46.37272541781826, 515.1266607594576, 361.4684070229754], "isController": true}, {"data": ["http://localhost:8080/api/v1/shopping-lists", 10000, 1731, 17.31, 373.48579999999964, 0, 3687, 293.0, 851.0, 1053.0, 1486.9899999999998, 46.4826573205537, 20.16473054294068, 53.20812588026533], "isController": false}, {"data": ["http://localhost:8080/api/v1/shopping-lists/{id} - GET", 20000, 3462, 17.31, 381.2563499999986, 0, 3418, 308.0, 857.9000000000015, 1055.0, 1500.9900000000016, 93.0427298736945, 84.85740020440325, 58.133624624921495], "isController": false}, {"data": ["http://localhost:8080/api/v1/shopping-lists/{id} - PATCH", 10000, 1731, 17.31, 384.7507000000012, 0, 2563, 316.0, 858.0, 1047.0, 1496.9799999999996, 46.523529274930794, 44.628440560375914, 58.09359934372747], "isController": false}, {"data": ["http://localhost:8080/api/v1/products", 20000, 3462, 17.31, 314.78745000000004, 0, 3328, 241.0, 745.0, 933.9500000000007, 1403.0, 92.95494473828535, 60.09283911416262, 54.67822266078882], "isController": false}, {"data": ["http://localhost:8080/api/v1/auth/access_token", 10000, 1731, 17.31, 471.2129999999995, 78, 3693, 410.0, 799.0, 1019.8999999999978, 1763.9599999999991, 46.392517814726844, 39.105471975729756, 21.519966759760987], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["401", 19041, 100.0, 17.31], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 110000, 19041, "401", 19041, "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["http://localhost:8080/api/v1/shopping-lists/{id} - DELETE", 10000, 1731, "401", 1731, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["http://localhost:8080/api/v1/shopping-lists?page=0", 30000, 5193, "401", 5193, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["http://localhost:8080/api/v1/shopping-lists", 10000, 1731, "401", 1731, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["http://localhost:8080/api/v1/shopping-lists/{id} - GET", 20000, 3462, "401", 3462, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["http://localhost:8080/api/v1/shopping-lists/{id} - PATCH", 10000, 1731, "401", 1731, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["http://localhost:8080/api/v1/products", 20000, 3462, "401", 3462, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["http://localhost:8080/api/v1/auth/access_token", 10000, 1731, "401", 1731, "", "", "", "", "", "", "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
