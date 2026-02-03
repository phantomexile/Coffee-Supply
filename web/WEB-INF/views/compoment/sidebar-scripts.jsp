<!-- Sidebar JavaScript - Load this after other JS files -->
<script src="${pageContext.request.contextPath}/js/sidebar.js"></script>

<script>
$(document).ready(function() {
    // Ensure sidebar functionality is loaded
    if (typeof toggleInventoryMenu === 'undefined') {
        // Fallback functions if sidebar.js fails to load
        window.toggleInventoryMenu = function(element) {
            var menu = element.nextElementSibling;
            var icon = element.querySelector('.fa-angle-left');
            
            if (menu.style.display === 'none' || menu.style.display === '') {
                menu.style.display = 'block';
                icon.classList.add('fa-angle-down');
                icon.classList.remove('fa-angle-left');
                element.parentElement.classList.add('active');
            } else {
                menu.style.display = 'none';
                icon.classList.add('fa-angle-left');
                icon.classList.remove('fa-angle-down');
                element.parentElement.classList.remove('active');
            }
        };
        
        window.togglePurchaseMenu = window.toggleInventoryMenu;
    }
});
</script>