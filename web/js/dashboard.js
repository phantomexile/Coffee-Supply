/**
 * Dashboard Common JavaScript
 * Handles sidebar, navigation, and common dashboard functionality
 */

// Global dashboard state
const Dashboard = {
    sidebar: {
        collapsed: false,
        mobile: false
    },
    user: {
        role: null,
        name: null
    }
};

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeDashboard();
    initializeSidebar();
    initializeRoleBasedFeatures();
    initializeQuickActions();
    loadDashboardData();
});

/**
 * Initialize main dashboard functionality
 */
function initializeDashboard() {
    // Get user role from session/body class
    const bodyClasses = document.body.className;
    const roleMatch = bodyClasses.match(/role-(\w+)/);
    if (roleMatch) {
        Dashboard.user.role = roleMatch[1];
    }
    
    // Set active navigation item
    setActiveNavigation();
    
    // Initialize tooltips if Bootstrap is available
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
}

/**
 * Initialize sidebar functionality
 */
function initializeSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const sidebarToggle = document.querySelector('.sidebar-toggle');
    const mainContent = document.querySelector('.main-content');
    
    if (!sidebar || !sidebarToggle) return;
    
    // Check if mobile view
    Dashboard.sidebar.mobile = window.innerWidth <= 768;
    
    // Toggle sidebar on button click
    sidebarToggle.addEventListener('click', function() {
        toggleSidebar();
    });
    
    // Handle window resize
    window.addEventListener('resize', function() {
        const wasMobile = Dashboard.sidebar.mobile;
        Dashboard.sidebar.mobile = window.innerWidth <= 768;
        
        if (wasMobile !== Dashboard.sidebar.mobile) {
            if (Dashboard.sidebar.mobile) {
                sidebar.classList.remove('collapsed');
                sidebar.classList.remove('active');
            } else {
                sidebar.classList.remove('active');
                if (Dashboard.sidebar.collapsed) {
                    sidebar.classList.add('collapsed');
                }
            }
        }
    });
    
    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', function(e) {
        if (Dashboard.sidebar.mobile && 
            sidebar.classList.contains('active') &&
            !sidebar.contains(e.target) &&
            !sidebarToggle.contains(e.target)) {
            closeSidebar();
        }
    });
    
    // Load saved sidebar state
    loadSidebarState();
}

/**
 * Toggle sidebar visibility
 */
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    
    if (Dashboard.sidebar.mobile) {
        sidebar.classList.toggle('active');
    } else {
        sidebar.classList.toggle('collapsed');
        Dashboard.sidebar.collapsed = sidebar.classList.contains('collapsed');
        saveSidebarState();
    }
}

/**
 * Close sidebar (mobile only)
 */
function closeSidebar() {
    const sidebar = document.querySelector('.sidebar');
    if (Dashboard.sidebar.mobile) {
        sidebar.classList.remove('active');
    }
}

/**
 * Save sidebar state to localStorage
 */
function saveSidebarState() {
    localStorage.setItem('dashboard_sidebar_collapsed', Dashboard.sidebar.collapsed);
}

/**
 * Load sidebar state from localStorage
 */
function loadSidebarState() {
    const saved = localStorage.getItem('dashboard_sidebar_collapsed');
    if (saved === 'true' && !Dashboard.sidebar.mobile) {
        Dashboard.sidebar.collapsed = true;
        document.querySelector('.sidebar')?.classList.add('collapsed');
    }
}

/**
 * Set active navigation item based on current page
 */
function setActiveNavigation() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        link.classList.remove('active');
        
        const href = link.getAttribute('href');
        if (href && currentPath.includes(href)) {
            link.classList.add('active');
        }
    });
}

/**
 * Initialize role-based features
 */
function initializeRoleBasedFeatures() {
    const userRole = Dashboard.user.role;
    if (!userRole) return;
    
    // Add role class to body for CSS-based visibility
    document.body.classList.add(`role-${userRole}`);
    
    // Hide/show elements based on role
    hideElementsByRole(userRole);
    
    // Initialize role-specific functionality
    switch (userRole) {
        case 'admin':
            initializeAdminFeatures();
            break;
        case 'hr':
            initializeHRFeatures();
            break;
        case 'inventory':
            initializeInventoryFeatures();
            break;
        case 'barista':
            initializeBaristaFeatures();
            break;
    }
}

/**
 * Hide elements that user doesn't have permission to see
 */
function hideElementsByRole(role) {
    const restrictedElements = document.querySelectorAll('[data-role-required]');
    
    restrictedElements.forEach(element => {
        const requiredRoles = element.getAttribute('data-role-required').split(',');
        if (!requiredRoles.includes(role) && !requiredRoles.includes('admin')) {
            element.style.display = 'none';
        }
    });
}

/**
 * Initialize admin-specific features
 */
function initializeAdminFeatures() {
    console.log('Initializing admin features...');
    // Admin can see everything
}

/**
 * Initialize HR-specific features
 */
function initializeHRFeatures() {
    console.log('Initializing HR features...');
    // HR-specific functionality
}

/**
 * Initialize inventory staff features
 */
function initializeInventoryFeatures() {
    console.log('Initializing inventory features...');
    // Inventory-specific functionality
}

/**
 * Initialize barista features
 */
function initializeBaristaFeatures() {
    console.log('Initializing barista features...');
    // Barista-specific functionality
}

/**
 * Initialize quick actions
 */
function initializeQuickActions() {
    const quickActions = document.querySelectorAll('.quick-action');
    
    quickActions.forEach(action => {
        action.addEventListener('click', function(e) {
            // Add loading state
            this.classList.add('loading');
            
            // Remove loading state after navigation
            setTimeout(() => {
                this.classList.remove('loading');
            }, 1000);
        });
    });
}

/**
 * Load dashboard data
 */
function loadDashboardData() {
    // Load stats
    loadStats();
    
    // Load recent activities
    loadRecentActivities();
    
    // Auto-refresh data every 5 minutes
    setInterval(refreshDashboardData, 5 * 60 * 1000);
}

/**
 * Load statistics data
 */
function loadStats() {
    // This would typically make AJAX calls to get real data
    // For now, we'll simulate loading
    const statCards = document.querySelectorAll('.stat-card');
    
    statCards.forEach(card => {
        const value = card.querySelector('.stat-value');
        if (value && value.textContent === '0') {
            animateCounter(value, 0, Math.floor(Math.random() * 1000), 1000);
        }
    });
}

/**
 * Load recent activities
 */
function loadRecentActivities() {
    // Simulate loading recent activities
    const activitiesContainer = document.querySelector('.recent-activities');
    if (activitiesContainer) {
        // Add loading state
        activitiesContainer.classList.add('loading');
        
        // Simulate API call
        setTimeout(() => {
            activitiesContainer.classList.remove('loading');
        }, 1000);
    }
}

/**
 * Refresh dashboard data
 */
function refreshDashboardData() {
    console.log('Refreshing dashboard data...');
    loadStats();
    loadRecentActivities();
}

/**
 * Animate counter from start to end value
 */
function animateCounter(element, start, end, duration) {
    const startTime = performance.now();
    
    function updateCounter(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        const current = Math.floor(start + (end - start) * progress);
        element.textContent = current.toLocaleString();
        
        if (progress < 1) {
            requestAnimationFrame(updateCounter);
        }
    }
    
    requestAnimationFrame(updateCounter);
}

/**
 * Show notification
 */
function showNotification(message, type = 'info', duration = 5000) {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} notification`;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 10000;
        min-width: 300px;
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.15);
        transform: translateX(100%);
        transition: transform 0.3s ease;
    `;
    notification.innerHTML = `
        <div style="display: flex; align-items: center; justify-content: space-between;">
            <span>${message}</span>
            <button onclick="this.parentElement.parentElement.remove()" style="background: none; border: none; font-size: 18px; cursor: pointer; margin-left: 15px;">&times;</button>
        </div>
    `;
    
    document.body.appendChild(notification);
    
    // Animate in
    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 100);
    
    // Auto remove
    setTimeout(() => {
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, duration);
}

/**
 * Confirm action with modal
 */
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

/**
 * Handle logout
 */
function logout() {
    confirmAction('Bạn có chắc chắn muốn đăng xuất?', function() {
        // Clear local storage
        localStorage.removeItem('dashboard_sidebar_collapsed');
        
        // Redirect to logout
        window.location.href = '/login?logout=true';
    });
}

/**
 * Format number with locale
 */
function formatNumber(number) {
    return new Intl.NumberFormat('vi-VN').format(number);
}

/**
 * Format currency
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

/**
 * Format date
 */
function formatDate(date) {
    return new Intl.DateTimeFormat('vi-VN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(new Date(date));
}

/**
 * Debounce function for search inputs
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Handle search functionality
 */
function initializeSearch() {
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        const debouncedSearch = debounce(function(query) {
            performSearch(query);
        }, 300);
        
        searchInput.addEventListener('input', function() {
            debouncedSearch(this.value);
        });
    }
}

/**
 * Perform search (to be implemented based on specific needs)
 */
function performSearch(query) {
    console.log('Searching for:', query);
    // Implement search functionality
}

// Export functions for use in other files
window.Dashboard = Dashboard;
window.showNotification = showNotification;
window.confirmAction = confirmAction;
window.logout = logout;
window.formatNumber = formatNumber;
window.formatCurrency = formatCurrency;
window.formatDate = formatDate;