<%-- 
    Document   : tailwind
    Created on : Mar 10, 2023, 11:23:48 AM
    Author     : N4O
--%>

<script src="https://cdn.tailwindcss.com"></script>
<script src="https://cdn.tailwindcss.com?plugins=forms"></script>
<style type="text/tailwindcss">
    @layer components {
        .nav-link {
            @apply hover:underline hover:opacity-90 transition text-emerald-400;
        }
        .text-glow {
            text-shadow: 0 0 6px #fff;
        }
        .table-h {
            @apply border-b border-x border-slate-600 font-medium p-4 pl-8 pt-0 pb-3 text-slate-200 text-center;
        }
        .table-b {
            @apply border-b border-x border-slate-700 font-medium p-4 pl-8 text-slate-300 text-left;
        }
        .table-sales {
            @apply border-collapse table-auto w-full text-sm;
        }
        .table-btn-red {
            @apply px-2 py-1 rounded-md bg-red-600 text-white hover:opacity-90 cursor-pointer transition;
        }
        .table-btn-green {
            @apply px-2 py-1 rounded-md bg-emerald-600 text-white hover:opacity-90 cursor-pointer transition;
        }
        .table-btn-warn {
            @apply px-2 py-1 rounded-md bg-orange-500 text-white hover:opacity-90 cursor-pointer transition;
        }
        .no-mod {
            @apply text-center mt-2 text-gray-300 font-light;
        }
    }
</style>