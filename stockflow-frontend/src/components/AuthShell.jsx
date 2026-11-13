import { Link } from "react-router-dom";
import { Boxes } from "lucide-react";

export default function AuthShell({ children, title, subtitle }) {
  return (
    <div className="flex min-h-screen bg-gray-50">
      {/* Brand panel */}
      <div className="hidden w-1/2 flex-col justify-between bg-brand-700 p-12 text-white lg:flex">
        <div className="flex items-center gap-2">
          <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-white/15">
            <Boxes className="h-6 w-6" />
          </div>
          <span className="text-xl font-semibold">StockFlow</span>
        </div>
        <div className="max-w-md">
          <h2 className="text-3xl font-semibold leading-tight">
            Modern inventory management for growing teams.
          </h2>
          <p className="mt-4 text-brand-100">
            Track stock levels, monitor low-stock alerts, and manage your
            product catalog — all from one clean dashboard.
          </p>
        </div>
        <p className="text-sm text-brand-200">
          &copy; {new Date().getFullYear()} StockFlow. All rights reserved.
        </p>
      </div>

      {/* Form panel */}
      <div className="flex w-full flex-col justify-center px-6 py-12 lg:w-1/2 lg:px-20">
        <div className="mx-auto w-full max-w-sm">
          <div className="mb-8 flex items-center gap-2 lg:hidden">
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-brand-600 text-white">
              <Boxes className="h-6 w-6" />
            </div>
            <span className="text-xl font-semibold text-gray-900">StockFlow</span>
          </div>
          <h1 className="text-2xl font-semibold text-gray-900">{title}</h1>
          {subtitle && <p className="mt-2 text-sm text-gray-500">{subtitle}</p>}
          <div className="mt-8">{children}</div>
        </div>
      </div>
    </div>
  );
}

export function AuthLink({ to, children }) {
  return (
    <Link
      to={to}
      className="font-medium text-brand-600 hover:text-brand-700 transition-colors"
    >
      {children}
    </Link>
  );
}
