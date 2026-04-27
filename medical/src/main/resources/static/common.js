const common = {
    getLoggedInUser() {
        const raw = localStorage.getItem("user");
        if (!raw) return null;
        try {
            return JSON.parse(raw);
        } catch (e) {
            return null;
        }
    },

    checkLogin(role) {
        const user = this.getLoggedInUser();
        if (!user) {
            window.location.href = "login.html";
            return null;
        }
        if (role && user.role !== role) {
            alert("权限不足");
            window.location.href = "login.html";
            return null;
        }
        return user;
    },

    logout() {
        localStorage.removeItem("user");
        window.location.href = "login.html";
    },

    formatDateTime(dt) {
        if (!dt) return "—";
        try {
            const d = new Date(dt);
            if (isNaN(d.getTime())) return dt;
            const pad = n => n.toString().padStart(2, "0");
            return d.getFullYear() + "-" + pad(d.getMonth() + 1) + "-" + pad(d.getDate())
                + " " + pad(d.getHours()) + ":" + pad(d.getMinutes());
        } catch {
            return dt;
        }
    },

    async fetch(url, options = {}) {
        try {
            const res = await fetch(url, options);
            if (!res.ok) {
                const text = await res.text();
                throw new Error(text || `请求失败: ${res.status}`);
            }
            if (res.status === 204) return null;
            return await res.json();
        } catch (e) {
            console.error(`API Error (${url}):`, e);
            throw e;
        }
    },

    showMessage(type, text) {
        // Simple alert for now, can be improved to a toast
        alert(text);
    }
};
