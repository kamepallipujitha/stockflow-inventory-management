
import { useCallback, useEffect, useState } from "react";
import {
  Search,
  Plus,
  Pencil,
  Trash2,
  Package,
  AlertTriangle,
  CheckCircle,
  X,
  ArrowUpDown,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";

import {
  searchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
} from "../services/api";

import Modal from "../components/Modal";
import Spinner from "../components/Spinner";

const EMPTY_FORM = {
  name: "",
  sku: "",
  description: "",
  quantity: "",
  costPrice: "",
  sellingPrice: "",
  lowStockThreshold: "10",
};

function qtyOf(product) {
  return Number(product.quantity ?? 0);
}

function nameOf(product) {
  return product.name ?? "Unnamed product";
}

function StockBadge({ quantity, threshold }) {
  if (quantity <= 0) {
    return (
      <span className="inline-flex items-center gap-1 rounded-full bg-red-50 px-2.5 py-1 text-xs font-medium text-red-700">
        <X className="h-3 w-3" />
        Out of stock
      </span>
    );
  }

  if (quantity <= threshold) {
    return (
      <span className="inline-flex items-center gap-1 rounded-full bg-amber-50 px-2.5 py-1 text-xs font-medium text-amber-700">
        <AlertTriangle className="h-3 w-3" />
        Low ({quantity})
      </span>
    );
  }

  return (
    <span className="inline-flex items-center gap-1 rounded-full bg-green-50 px-2.5 py-1 text-xs font-medium text-green-700">
      <CheckCircle className="h-3 w-3" />
      In stock ({quantity})
    </span>
  );
}

export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Search
  const [query, setQuery] = useState("");
  const [debouncedQuery, setDebouncedQuery] = useState("");

  // Pagination
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [first, setFirst] = useState(true);
  const [last, setLast] = useState(true);

  // Backend sorting
  const [sortKey, setSortKey] = useState("name");
  const [sortDir, setSortDir] = useState("asc");

  // Add/Edit modal
  const [formOpen, setFormOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY_FORM);
  const [formError, setFormError] = useState("");
  const [saving, setSaving] = useState(false);

  // Delete modal
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleting, setDeleting] = useState(false);

  // Debounce search input
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedQuery(query.trim());
      setPage(0);
    }, 350);

    return () => clearTimeout(timer);
  }, [query]);

  // Fetch products using backend search, pagination and sorting
  const fetchProducts = useCallback(async () => {
    setLoading(true);
    setError("");

    try {
      const { data } = await searchProducts({
        keyword: debouncedQuery,
        page,
        size,
        sortBy: sortKey,
        direction: sortDir,
      });

      setProducts(data.content ?? []);
      setTotalElements(data.totalElements ?? 0);
      setTotalPages(data.totalPages ?? 0);
      setFirst(data.first ?? true);
      setLast(data.last ?? true);

      return data;
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.response?.data?.error ||
          err.message ||
          "Failed to load products."
      );

      return null;
    } finally {
      setLoading(false);
    }
  }, [page, size, sortKey, sortDir, debouncedQuery]);

  // Fetch whenever page, size, sorting or search changes
  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  // Backend search already returns filtered products
  const filtered = products;

  // Backend sorting
  const toggleSort = (key) => {
    if (sortKey === key) {
      setSortDir((direction) =>
        direction === "asc" ? "desc" : "asc"
      );
    } else {
      setSortKey(key);
      setSortDir("asc");
    }

    setPage(0);
  };

  // Open Add modal
  const openAdd = () => {
    setEditing(null);
    setForm({ ...EMPTY_FORM });
    setFormError("");
    setFormOpen(true);
  };

  // Open Edit modal
  const openEdit = (product) => {
    setEditing(product);

    setForm({
      name: product.name ?? "",
      sku: product.sku ?? "",
      description: product.description ?? "",
      quantity: String(product.quantity ?? 0),
      costPrice: String(product.costPrice ?? ""),
      sellingPrice: String(product.sellingPrice ?? ""),
      lowStockThreshold: String(
        product.lowStockThreshold ?? 10
      ),
    });

    setFormError("");
    setFormOpen(true);
  };

  // Close modal
  const closeForm = () => {
    setFormOpen(false);
    setEditing(null);
    setForm({ ...EMPTY_FORM });
    setFormError("");
  };

  // Save product
  const handleSave = async (e) => {
    e.preventDefault();
    setFormError("");

    if (!form.name.trim()) {
      setFormError("Product name is required.");
      return;
    }

    if (!form.sku.trim()) {
      setFormError("SKU is required.");
      return;
    }

    if (
      form.quantity.trim() === "" ||
      !Number.isInteger(Number(form.quantity)) ||
      Number(form.quantity) < 0
    ) {
      setFormError(
        "Quantity must be a valid non-negative integer."
      );
      return;
    }

    if (
      form.costPrice.trim() === "" ||
      !Number.isFinite(Number(form.costPrice)) ||
      Number(form.costPrice) < 0
    ) {
      setFormError("Enter a valid non-negative cost price.");
      return;
    }

    if (
      form.sellingPrice.trim() === "" ||
      !Number.isFinite(Number(form.sellingPrice)) ||
      Number(form.sellingPrice) < 0
    ) {
      setFormError("Enter a valid non-negative selling price.");
      return;
    }

    if (
      form.lowStockThreshold.trim() === "" ||
      !Number.isInteger(Number(form.lowStockThreshold)) ||
      Number(form.lowStockThreshold) < 0
    ) {
      setFormError(
        "Low-stock threshold must be a non-negative integer."
      );
      return;
    }

    const payload = {
      name: form.name.trim(),
      sku: form.sku.trim(),
      description: form.description.trim(),
      quantity: Number(form.quantity),
      costPrice: Number(form.costPrice),
      sellingPrice: Number(form.sellingPrice),
      lowStockThreshold: Number(form.lowStockThreshold),
    };

    setSaving(true);

    try {
      if (editing) {
        await updateProduct(editing.id, payload);
        closeForm();
        await fetchProducts();
      } else {
        await createProduct(payload);
        closeForm();

        if (page === 0) {
          await fetchProducts();
        } else {
          setPage(0);
        }
      }
    } catch (err) {
      setFormError(
        err.response?.data?.message ||
          err.response?.data?.error ||
          err.message ||
          "Something went wrong. Please try again."
      );
    } finally {
      setSaving(false);
    }
  };

  // Delete product
  const handleDelete = async () => {
    if (!deleteTarget) return;

    setDeleting(true);

    try {
      await deleteProduct(deleteTarget.id);
      setDeleteTarget(null);

      const data = await fetchProducts();

      // Move to previous page if the current page becomes empty
      if (
        data &&
        data.content?.length === 0 &&
        page > 0
      ) {
        setPage((currentPage) => currentPage - 1);
      }
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.response?.data?.error ||
          err.message ||
          "Failed to delete product."
      );

      setDeleteTarget(null);
    } finally {
      setDeleting(false);
    }
  };

  // Change page size
  const handlePageSizeChange = (e) => {
    setSize(Number(e.target.value));
    setPage(0);
  };

  return (
    <div className="space-y-5">

      {/* Toolbar */}
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">

        <div className="relative w-full sm:max-w-xs">
          <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />

          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search by product name, SKU or description..."
            className="w-full rounded-lg border border-gray-300 py-2.5 pl-10 pr-3 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
          />
        </div>

        <button
          onClick={openAdd}
          className="flex items-center justify-center gap-2 rounded-lg bg-brand-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-brand-700"
        >
          <Plus className="h-4 w-4" />
          Add Product
        </button>

      </div>

      {/* Error banner */}
      {error && (
        <div className="flex items-start gap-2 rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700">
          <AlertTriangle className="mt-0.5 h-4 w-4 shrink-0" />
          <span>{error}</span>
        </div>
      )}

      {/* Products table */}
      <div className="overflow-hidden rounded-xl border border-gray-200 bg-white shadow-card">

        <div className="overflow-x-auto">

          <table className="w-full min-w-[750px] text-left text-sm">

            <thead className="border-b border-gray-200 bg-gray-50 text-xs uppercase tracking-wider text-gray-500">

              <tr>

                <th className="px-6 py-3 font-semibold">
                  <button
                    onClick={() => toggleSort("name")}
                    className="flex items-center gap-1 hover:text-gray-700"
                  >
                    Product
                    <ArrowUpDown className="h-3 w-3" />
                    {sortKey === "name" && (
                      <span>{sortDir === "asc" ? "↑" : "↓"}</span>
                    )}
                  </button>
                </th>

                <th className="px-6 py-3 font-semibold">
                  SKU
                </th>

                <th className="px-6 py-3 font-semibold">
                  Description
                </th>

                <th className="px-6 py-3 font-semibold">
                  <button
                    onClick={() => toggleSort("quantity")}
                    className="flex items-center gap-1 hover:text-gray-700"
                  >
                    Stock Status
                    <ArrowUpDown className="h-3 w-3" />
                    {sortKey === "quantity" && (
                      <span>{sortDir === "asc" ? "↑" : "↓"}</span>
                    )}
                  </button>
                </th>

                <th className="px-6 py-3 font-semibold">
                  Cost Price
                </th>

                <th className="px-6 py-3 font-semibold">
                  <button
                    onClick={() => toggleSort("sellingPrice")}
                    className="flex items-center gap-1 hover:text-gray-700"
                  >
                    Selling Price
                    <ArrowUpDown className="h-3 w-3" />
                    {sortKey === "sellingPrice" && (
                      <span>{sortDir === "asc" ? "↑" : "↓"}</span>
                    )}
                  </button>
                </th>

                <th className="px-6 py-3 text-right font-semibold">
                  Actions
                </th>

              </tr>

            </thead>

            <tbody className="divide-y divide-gray-100">

              {loading ? (
                <tr>
                  <td
                    colSpan={7}
                    className="px-6 py-12 text-center text-gray-400"
                  >
                    Loading products...
                  </td>
                </tr>
              ) : filtered.length === 0 ? (
                <tr>
                  <td colSpan={7} className="px-6 py-12">

                    <div className="flex flex-col items-center justify-center text-center">

                      <div className="flex h-12 w-12 items-center justify-center rounded-full bg-gray-100 text-gray-400">
                        <Package className="h-6 w-6" />
                      </div>

                      <p className="mt-3 text-sm font-medium text-gray-700">
                        No products found
                      </p>

                      <p className="mt-1 text-xs text-gray-500">
                        {debouncedQuery
                          ? "No matching products found. Try another search term."
                          : "Click 'Add Product' to create your first item."}
                      </p>

                    </div>

                  </td>
                </tr>
              ) : (
                filtered.map((product) => {
                  const quantity = qtyOf(product);
                  const costPrice = Number(product.costPrice ?? 0);
                  const sellingPrice = Number(
                    product.sellingPrice ?? 0
                  );
                  const threshold = Number(
                    product.lowStockThreshold ?? 10
                  );

                  return (
                    <tr
                      key={product.id}
                      className="transition hover:bg-gray-50"
                    >

                      <td className="px-6 py-4">
                        <div className="flex items-center gap-3">

                          <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-brand-50 text-brand-600">
                            <Package className="h-4 w-4" />
                          </div>

                          <span className="font-medium text-gray-900">
                            {nameOf(product)}
                          </span>

                        </div>
                      </td>

                      <td className="px-6 py-4 text-gray-600">
                        {product.sku || "—"}
                      </td>

                      <td className="px-6 py-4 text-gray-600">
                        {product.description || "—"}
                      </td>

                      <td className="px-6 py-4">
                        <StockBadge
                          quantity={quantity}
                          threshold={threshold}
                        />
                      </td>

                      <td className="px-6 py-4 text-gray-900">
                        {costPrice.toFixed(2)}
                      </td>

                      <td className="px-6 py-4 text-gray-900">
                        {sellingPrice.toFixed(2)}
                      </td>

                      <td className="px-6 py-4">
                        <div className="flex items-center justify-end gap-1">

                          <button
                            onClick={() => openEdit(product)}
                            className="rounded-lg p-2 text-gray-500 transition hover:bg-brand-50 hover:text-brand-600"
                            aria-label="Edit product"
                          >
                            <Pencil className="h-4 w-4" />
                          </button>

                          <button
                            onClick={() => setDeleteTarget(product)}
                            className="rounded-lg p-2 text-gray-500 transition hover:bg-red-50 hover:text-red-600"
                            aria-label="Delete product"
                          >
                            <Trash2 className="h-4 w-4" />
                          </button>

                        </div>
                      </td>

                    </tr>
                  );
                })
              )}

            </tbody>

          </table>

        </div>

        {/* Pagination controls */}
        <div className="flex flex-col gap-4 border-t border-gray-200 px-4 py-4 sm:flex-row sm:items-center sm:justify-between">

          <div className="flex flex-wrap items-center gap-3 text-sm text-gray-600">

            <span>
              Total products: <strong>{totalElements}</strong>
            </span>

            <label className="flex items-center gap-2">
              Rows per page:

              <select
                value={size}
                onChange={handlePageSizeChange}
                className="rounded-md border border-gray-300 bg-white px-2 py-1.5 text-sm outline-none focus:border-brand-500"
              >
                <option value={5}>5</option>
                <option value={10}>10</option>
                <option value={20}>20</option>
              </select>
            </label>

          </div>

          <div className="flex items-center justify-between gap-3">

            <span className="text-sm text-gray-600">
              Page {totalPages === 0 ? 0 : page + 1} of {totalPages}
            </span>

            <div className="flex items-center gap-2">

              <button
                type="button"
                onClick={() => setPage((current) => current - 1)}
                disabled={first || loading}
                className="flex items-center gap-1 rounded-lg border border-gray-300 px-3 py-2 text-sm text-gray-700 transition hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-40"
              >
                <ChevronLeft className="h-4 w-4" />
                Previous
              </button>

              <button
                type="button"
                onClick={() => setPage((current) => current + 1)}
                disabled={last || loading}
                className="flex items-center gap-1 rounded-lg border border-gray-300 px-3 py-2 text-sm text-gray-700 transition hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-40"
              >
                Next
                <ChevronRight className="h-4 w-4" />
              </button>

            </div>

          </div>

        </div>

      </div>

      {/* Add / Edit modal */}
      <Modal
        open={formOpen}
        title={editing ? "Edit Product" : "Add Product"}
        onClose={closeForm}
      >

        <form onSubmit={handleSave} className="space-y-4">

          {formError && (
            <div className="flex items-start gap-2 rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700">
              <AlertTriangle className="mt-0.5 h-4 w-4 shrink-0" />
              <span>{formError}</span>
            </div>
          )}

          {/* Product name */}
          <div>
            <label className="mb-1.5 block text-sm font-medium text-gray-700">
              Product Name
            </label>

            <input
              type="text"
              value={form.name}
              onChange={(e) =>
                setForm({ ...form, name: e.target.value })
              }
              required
              placeholder="e.g. Wireless Mouse"
              className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
            />
          </div>

          {/* SKU */}
          <div>
            <label className="mb-1.5 block text-sm font-medium text-gray-700">
              SKU
            </label>

            <input
              type="text"
              value={form.sku}
              onChange={(e) =>
                setForm({ ...form, sku: e.target.value })
              }
              required
              placeholder="e.g. WM-001"
              className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
            />
          </div>

          {/* Description */}
          <div>
            <label className="mb-1.5 block text-sm font-medium text-gray-700">
              Description
            </label>

            <textarea
              value={form.description}
              onChange={(e) =>
                setForm({
                  ...form,
                  description: e.target.value,
                })
              }
              placeholder="Enter product description"
              rows={2}
              className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
            />
          </div>

          {/* Quantity and threshold */}
          <div className="grid grid-cols-2 gap-4">

            <div>
              <label className="mb-1.5 block text-sm font-medium text-gray-700">
                Quantity
              </label>

              <input
                type="number"
                min="0"
                step="1"
                value={form.quantity}
                onChange={(e) =>
                  setForm({
                    ...form,
                    quantity: e.target.value,
                  })
                }
                required
                placeholder="0"
                className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
              />
            </div>

            <div>
              <label className="mb-1.5 block text-sm font-medium text-gray-700">
                Low Stock Threshold
              </label>

              <input
                type="number"
                min="0"
                step="1"
                value={form.lowStockThreshold}
                onChange={(e) =>
                  setForm({
                    ...form,
                    lowStockThreshold: e.target.value,
                  })
                }
                required
                placeholder="10"
                className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
              />
            </div>

          </div>

          {/* Cost and selling price */}
          <div className="grid grid-cols-2 gap-4">

            <div>
              <label className="mb-1.5 block text-sm font-medium text-gray-700">
                Cost Price
              </label>

              <input
                type="number"
                min="0"
                step="0.01"
                value={form.costPrice}
                onChange={(e) =>
                  setForm({
                    ...form,
                    costPrice: e.target.value,
                  })
                }
                required
                placeholder="0.00"
                className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
              />
            </div>

            <div>
              <label className="mb-1.5 block text-sm font-medium text-gray-700">
                Selling Price
              </label>

              <input
                type="number"
                min="0"
                step="0.01"
                value={form.sellingPrice}
                onChange={(e) =>
                  setForm({
                    ...form,
                    sellingPrice: e.target.value,
                  })
                }
                required
                placeholder="0.00"
                className="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
              />
            </div>

          </div>

          {/* Buttons */}
          <div className="flex justify-end gap-3 pt-2">

            <button
              type="button"
              onClick={closeForm}
              className="rounded-lg border border-gray-300 px-4 py-2.5 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={saving}
              className="flex items-center gap-2 rounded-lg bg-brand-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-brand-700 disabled:cursor-not-allowed disabled:opacity-70"
            >
              {saving && <Spinner className="h-4 w-4" />}
              {editing ? "Save Changes" : "Add Product"}
            </button>

          </div>

        </form>

      </Modal>

      {/* Delete confirmation */}
      <Modal
        open={!!deleteTarget}
        title="Delete Product"
        onClose={() => setDeleteTarget(null)}
      >

        <p className="text-sm text-gray-600">
          Are you sure you want to delete{" "}
          <span className="font-semibold text-gray-900">
            {deleteTarget ? nameOf(deleteTarget) : ""}
          </span>
          ? This action cannot be undone.
        </p>

        <div className="mt-6 flex justify-end gap-3">

          <button
            type="button"
            onClick={() => setDeleteTarget(null)}
            className="rounded-lg border border-gray-300 px-4 py-2.5 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
          >
            Cancel
          </button>

          <button
            type="button"
            onClick={handleDelete}
            disabled={deleting}
            className="flex items-center gap-2 rounded-lg bg-red-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-red-700 disabled:cursor-not-allowed disabled:opacity-70"
          >
            {deleting && <Spinner className="h-4 w-4" />}
            Delete
          </button>

        </div>

      </Modal>

    </div>
  );
}
