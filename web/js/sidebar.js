// Sidebar Navigation Functions

// General toggle menu function for all menus
function toggleMenu(element) {
    var menu = element.nextElementSibling;
    var icon = element.querySelector('.fa-angle-left, .fa-angle-down');
    
    if (menu && (menu.style.display === 'none' || menu.style.display === '')) {
        menu.style.display = 'block';
        if (icon) {
            icon.classList.add('fa-angle-down');
            icon.classList.remove('fa-angle-left');
        }
        element.parentElement.classList.add('active');
    } else if (menu) {
        menu.style.display = 'none';
        if (icon) {
            icon.classList.add('fa-angle-left');
            icon.classList.remove('fa-angle-down');
        }
        element.parentElement.classList.remove('active');
    }
}

// Đảm bảo function có sẵn globally
window.toggleMenu = toggleMenu;

function toggleInventoryMenu(element) {
    toggleMenu(element);
}

function togglePurchaseMenu(element) {
    toggleMenu(element);
}

// Đảm bảo tất cả functions có sẵn globally
window.toggleInventoryMenu = toggleInventoryMenu;
window.togglePurchaseMenu = togglePurchaseMenu;

// Set active menu based on current URL
document.addEventListener('DOMContentLoaded', function() {
    var currentPath = window.location.pathname;
    var menuItems = document.querySelectorAll('.sidebar-menu a');
    
    menuItems.forEach(function(item) {
        var href = item.getAttribute('href');
        if (href && currentPath.includes(href.split('?')[0])) {
            item.parentElement.classList.add('active');
            
            // If it's a submenu item, expand the parent menu
            var parentTreeview = item.closest('.treeview');
            if (parentTreeview) {
                var parentMenu = parentTreeview.querySelector('.treeview-menu');
                var parentIcon = parentTreeview.querySelector('.fa-angle-left');
                
                if (parentMenu) {
                    parentMenu.style.display = 'block';
                    if (parentIcon) {
                        parentIcon.classList.add('fa-angle-down');
                        parentIcon.classList.remove('fa-angle-left');
                    }
                    parentTreeview.classList.add('active');
                }
            }
        }
    });
});