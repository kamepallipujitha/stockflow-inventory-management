
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  Package,
  Layers,
  AlertTriangle,
  TrendingUp,
  ArrowRight,
} from "lucide-react";

import { getDashboard } from "../services/api";

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch dashboard data from Spring Boot backend
  useEffect(() => {
    let active = true;

    const fetchDashboard = async () => {
      try {
        setLoading(true);
        setError("");

        const response = await getDashboard();

        if (active) {
          setStats(response.data);
        }
      } catch (err) {
        if (active) {
          setError(
            err.response?.data?.message ||
            err.response?.data?.error ||
            err.message ||
            "Failed to load dashboard data."
          );
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    };

    fetchDashboard();

    return () => {
      active = false;
    };
  }, []);

  // Match the backend DashboardResponse fields
  const totalProducts = stats?.totalProducts ?? 0;

  const totalQuantity = stats?.totalQuantity ?? 0;

  const lowStockItems = Array.isArray(stats?.lowStockProducts)
    ? stats.lowStockProducts
    : [];

  const lowStockCount = lowStockItems.length;

  // Dashboard statistic cards
  const cards = [
    {
      label: "Total Products",
      value: totalProducts,
      icon: Package,
      tint: "bg-brand-50 text-brand-600",
    },
    {
      label: "Total Quantity",
      value: totalQuantity,
      icon: Layers,
      tint: "bg-blue-50 text-blue-600",
    },
    {
      label: "Low Stock Products",
      value: lowStockCount,
      icon: AlertTriangle,
      tint: "bg-amber-50 text-amber-600",
    },
  ];

  // Loading state
  if (loading) {
    return (
      <div className="flex h-64 items-center justify-center text-gray-400">
        Loading dashboard...
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="rounded-xl border border-red-100 bg-red-50 px-6 py-8 text-center">
        <AlertTriangle className="mx-auto h-8 w-8 text-red-500" />

        <p className="mt-3 text-sm font-medium text-red-700">
          {error}
        </p>

        <p className="mt-1 text-xs text-red-500">
          Make sure your Spring Boot backend is running on
          http://localhost:8085
        </p>

        <button
          onClick={() => window.location.reload()}
          className="mt-4 rounded-lg bg-red-600 px-4 py-2 text-sm font-medium text-white hover:bg-red-700"
        >
          Retry
        </button>
      </div>
    );
  }

  return (
    <div className="space-y-6">

      {/* Dashboard Statistics */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">

        {cards.map(({ label, value, icon: Icon, tint }) => (
          <div
            key={label}
            className="rounded-xl border border-gray-200 bg-white p-6 shadow-card transition hover:shadow-cardHover"
          >
            <div className="flex items-center justify-between">

              <div>
                <p className="text-sm font-medium text-gray-500">
                  {label}
                </p>

                <p className="mt-2 text-3xl font-semibold text-gray-900">
                  {Number(value).toLocaleString()}
                </p>
              </div>

              <div
                className={`flex h-12 w-12 items-center justify-center rounded-lg ${tint}`}
              >
                <Icon className="h-6 w-6" />
              </div>

            </div>
          </div>
        ))}

      </div>

      {/* Low Stock Alerts */}
      <div className="rounded-xl border border-gray-200 bg-white shadow-card">

        <div className="flex items-center justify-between border-b border-gray-200 px-6 py-4">

          <div className="flex items-center gap-2">
            <TrendingUp className="h-5 w-5 text-amber-500" />

            <h2 className="text-base font-semibold text-gray-900">
              Low Stock Alerts
            </h2>
          </div>

          <Link
            to="/products"
            className="flex items-center gap-1 text-sm font-medium text-brand-600 hover:text-brand-700"
          >
            View all
            <ArrowRight className="h-4 w-4" />
          </Link>

        </div>

        {/* No low-stock products */}
        {lowStockItems.length === 0 ? (

          <div className="px-6 py-10 text-center text-sm text-gray-500">
            No low-stock products. Everything is well stocked.
          </div>

        ) : (

          /* Display low-stock products */
          <ul className="divide-y divide-gray-100">

            {lowStockItems.slice(0, 6).map((product) => (

              <li
                key={product.id}
                className="flex items-center justify-between px-6 py-4"
              >

                <div className="flex items-center gap-3">

                  <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-gray-100 text-gray-500">
                    <Package className="h-4 w-4" />
                  </div>

                  <div>
                    <p className="text-sm font-medium text-gray-900">
                      {product.name}
                    </p>

                    <p className="text-xs text-gray-500">
                      SKU: {product.sku}
                    </p>
                  </div>

                </div>

                <span className="inline-flex items-center gap-1 rounded-full bg-amber-50 px-2.5 py-1 text-xs font-medium text-amber-700">

                  <AlertTriangle className="h-3 w-3" />

                  {product.quantity} left

                </span>

              </li>

            ))}

          </ul>

        )}

      </div>

    </div>
  );
}
