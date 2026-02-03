// Inventory Dashboard JavaScript
// Wait for jQuery to be available
function waitForJQuery(callback) {
    if (typeof jQuery !== 'undefined') {
        callback();
    } else {
        console.log('Waiting for jQuery...');
        setTimeout(function() {
            waitForJQuery(callback);
        }, 100);
    }
}

// Initialize when both jQuery and DOM are ready
waitForJQuery(function() {
    $(document).ready(function() {
        console.log('Dashboard script loaded with jQuery');
        
        // Check if Chart.js is loaded
        if (typeof Chart === 'undefined') {
            console.error('Chart.js is not loaded');
            // Try to load Chart.js if not available
            loadChartJS().then(() => {
                initializeDashboard();
            }).catch(() => {
                console.error('Failed to load Chart.js');
                showAllChartsError();
            });
        } else {
            console.log('Chart.js is available');
            initializeDashboard();
        }
    });
});

function loadChartJS() {
    return new Promise((resolve, reject) => {
        if (typeof Chart !== 'undefined') {
            resolve();
            return;
        }
        
        const script = document.createElement('script');
        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js';
        script.onload = resolve;
        script.onerror = reject;
        document.head.appendChild(script);
    });
}

function initializeDashboard() {
    console.log('Initializing dashboard...');
    
    // Get data from hidden script tag
    var chartData = {};
    try {
        var chartDataElement = document.getElementById('chartData');
        if (chartDataElement) {
            var dataText = chartDataElement.textContent || chartDataElement.innerText;
            console.log('Raw chart data:', dataText);
            chartData = JSON.parse(dataText);
            console.log('Parsed chart data:', chartData);
        } else {
            console.warn('Chart data element not found, using fallback data');
            chartData = getFallbackData();
        }
    } catch (e) {
        console.error('Error parsing chart data:', e);
        chartData = getFallbackData();
    }
    
    // Initialize Charts with delay to ensure DOM is ready
    setTimeout(() => {
        initStockStatusChart(chartData);
        initCategoryChart();
        initStockLevelsChart(chartData);
        initDashboardFeatures();
    }, 100);
}

function getFallbackData() {
    return {
        "stockGoodCount": 0,
        "stockLowCount": 0,
        "stockOutCount": 0,
        "ingredients": []
    };
}

function showChartError(chartId) {
    var chartContainer = $('#' + chartId).parent();
    if (chartContainer.length > 0) {
        chartContainer.html('<div class="alert alert-warning text-center"><i class="fa fa-warning"></i> Không thể tải biểu đồ</div>');
    }
}

function showAllChartsError() {
    $('.chart-container canvas').each(function() {
        var chartId = $(this).attr('id');
        if (chartId) {
            showChartError(chartId);
        }
    });
}

function initStockStatusChart(data) {
    console.log('Initializing stock status chart with data:', data);
    
    var ctx = document.getElementById('stockStatusChart');
    if (!ctx) {
        console.error('Stock status chart canvas not found');
        return;
    }
    
    console.log('Canvas element found:', ctx);
    ctx = ctx.getContext('2d');
    
    // Hide loading, show canvas
    $('#stockStatusLoading').hide();
    $('#stockStatusChart').show();
    
    // Ensure we have valid data
    var goodCount = data.stockGoodCount || 0;
    var lowCount = data.stockLowCount || 0;
    var outCount = data.stockOutCount || 0;
    
    console.log('Chart data values:', {goodCount, lowCount, outCount});
    
    try {
        var stockStatusChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Đủ hàng', 'Sắp hết', 'Hết hàng'],
                datasets: [{
                    data: [goodCount, lowCount, outCount],
                    backgroundColor: [
                        '#00a65a',
                        '#f39c12', 
                        '#dd4b39'
                    ],
                    borderWidth: 2,
                    borderColor: '#fff',
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                var label = context.label || '';
                                var value = context.parsed || 0;
                                var total = context.dataset.data.reduce((a, b) => a + b, 0);
                                var percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0;
                                return label + ': ' + value + ' (' + percentage + '%)';
                            }
                        }
                    }
                }
            }
        });
        console.log('Stock status chart created successfully');
    } catch (e) {
        console.error('Error creating stock status chart:', e);
        $('#stockStatusLoading').html('<div class="alert alert-warning"><i class="fa fa-warning"></i> Không thể tải biểu đồ</div>');
    }
}

function initCategoryChart() {
    console.log('Initializing category chart');
    
    var ctx = document.getElementById('categoryChart');
    if (!ctx) {
        console.error('Category chart canvas not found');
        return;
    }
    
    console.log('Category chart canvas found:', ctx);
    ctx = ctx.getContext('2d');
    
    // Hide loading, show canvas
    $('#categoryLoading').hide();
    $('#categoryChart').show();
    
    try {
        var categoryChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['Cà phê', 'Sữa', 'Đường', 'Syrup', 'Khác'],
                datasets: [{
                    data: [30, 20, 15, 25, 10],
                    backgroundColor: [
                        '#3c8dbc',
                        '#00a65a',
                        '#f39c12',
                        '#dd4b39',
                        '#605ca8'
                    ],
                    borderWidth: 2,
                    borderColor: '#fff',
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                var label = context.label || '';
                                var value = context.parsed || 0;
                                var total = context.dataset.data.reduce((a, b) => a + b, 0);
                                var percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0;
                                return label + ': ' + percentage + '%';
                            }
                        }
                    }
                }
            }
        });
        console.log('Category chart created successfully');
    } catch (e) {
        console.error('Error creating category chart:', e);
        $('#categoryLoading').html('<div class="alert alert-warning"><i class="fa fa-warning"></i> Không thể tải biểu đồ</div>');
    }
}

function initStockLevelsChart(data) {
    console.log('Initializing stock levels chart with data:', data);
    
    var ctx = document.getElementById('stockLevelsChart');
    if (!ctx) {
        console.error('Stock levels chart canvas not found');
        return;
    }
    
    console.log('Stock levels chart canvas found:', ctx);
    ctx = ctx.getContext('2d');
    
    // Hide loading, show canvas
    $('#stockLevelsLoading').hide();
    $('#stockLevelsChart').show();
    
    var labels = [];
    var currentStockData = [];
    var minStockData = [];
    
    // Extract data from ingredients array
    if (data.ingredients && data.ingredients.length > 0) {
        console.log('Using ingredients data:', data.ingredients);
        data.ingredients.forEach(function(ingredient) {
            labels.push(ingredient.name);
            currentStockData.push(ingredient.currentStock);
            minStockData.push(ingredient.minStock);
        });
    } else {
        console.warn('No ingredients data, using fallback');
        // Fallback data
        labels = ['Cà phê hạt', 'Sữa tươi', 'Đường trắng', 'Vanilla Syrup'];
        currentStockData = [25, 5, 30, 12];
        minStockData = [10, 15, 20, 8];
    }
    
    console.log('Chart data prepared:', {labels, currentStockData, minStockData});
    
    try {
        var stockLevelsChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Tồn kho hiện tại',
                    data: currentStockData,
                    backgroundColor: '#3c8dbc',
                    borderColor: '#367fa9',
                    borderWidth: 1,
                    borderRadius: 4,
                    borderSkipped: false
                }, {
                    label: 'Mức tối thiểu',
                    data: minStockData,
                    backgroundColor: '#dd4b39',
                    borderColor: '#d73925',
                    borderWidth: 1,
                    borderRadius: 4,
                    borderSkipped: false
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: '#e0e0e0'
                        },
                        ticks: {
                            callback: function(value) {
                                return value;
                            }
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false,
                        callbacks: {
                            label: function(context) {
                                return context.dataset.label + ': ' + context.parsed.y;
                            }
                        }
                    }
                },
                interaction: {
                    mode: 'nearest',
                    axis: 'x',
                    intersect: false
                }
            }
        });
        console.log('Stock levels chart created successfully');
    } catch (e) {
        console.error('Error creating stock levels chart:', e);
        $('#stockLevelsLoading').html('<div class="alert alert-warning"><i class="fa fa-warning"></i> Không thể tải biểu đồ</div>');
    }
}

function initDashboardFeatures() {
    // Add click handlers for info boxes
    $('.small-box').hover(
        function() {
            $(this).addClass('elevation-2');
        },
        function() {
            $(this).removeClass('elevation-2');
        }
    );
    
    // Add animation to activity items
    $('.activity-item').each(function(index) {
        $(this).delay(index * 100).fadeIn(300);
    });
    
    // Auto refresh functionality
    var refreshInterval = 5 * 60 * 1000; // 5 minutes
    var refreshTimer = setTimeout(function() {
        if (confirm('Dữ liệu đã được cập nhật. Bạn có muốn tải lại trang không?')) {
            location.reload();
        }
    }, refreshInterval);
    
    // Add refresh button handler
    $(document).on('click', '.btn-refresh', function() {
        location.reload();
    });
}

// Additional dashboard functions
function refreshDashboard() {
    location.reload();
}

function exportChart(chartId) {
    try {
        var canvas = document.getElementById(chartId);
        if (!canvas) {
            alert('Không tìm thấy biểu đồ để xuất!');
            return;
        }
        
        var url = canvas.toDataURL('image/png');
        var link = document.createElement('a');
        link.download = chartId + '_' + new Date().getTime() + '.png';
        link.href = url;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } catch (e) {
        console.error('Error exporting chart:', e);
        alert('Có lỗi xảy ra khi xuất biểu đồ!');
    }
}

// Add global error handler for charts
window.addEventListener('error', function(e) {
    if (e.message.includes('Chart')) {
        console.error('Chart error:', e);
    }
});