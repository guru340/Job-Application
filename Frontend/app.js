const state = {
  apiBase: localStorage.getItem("jobAppApiBase") || "http://localhost:8084",
  jobs: [],
  companies: [],
  reviews: [],
  selectedReviewCompanyId: "",
};

const $ = (selector) => document.querySelector(selector);

const els = {
  apiBase: $("#api-base"),
  saveApi: $("#save-api"),
  notice: $("#notice"),
  statusDot: $("#status-dot"),
  connectionText: $("#connection-text"),
  viewTitle: $("#view-title"),
  jobCount: $("#job-count"),
  companyCount: $("#company-count"),
  ratingAverage: $("#rating-average"),
  jobsList: $("#jobs-list"),
  companiesList: $("#companies-list"),
  reviewsList: $("#reviews-list"),
  jobCompany: $("#job-company"),
  reviewCompany: $("#review-company"),
  reviewCompanyFilter: $("#review-company-filter"),
  jobForm: $("#job-form"),
  companyForm: $("#company-form"),
  reviewForm: $("#review-form"),
};

els.apiBase.value = state.apiBase;

function endpoint(path) {
  return `${state.apiBase.replace(/\/$/, "")}${path}`;
}

async function request(path, options = {}) {
  const response = await fetch(endpoint(path), {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options,
  });

  if (!response.ok) {
    const body = await response.text();
    throw new Error(body || `Request failed with ${response.status}`);
  }

  const contentType = response.headers.get("content-type") || "";
  return contentType.includes("application/json") ? response.json() : response.text();
}

function showNotice(message, type = "success") {
  els.notice.textContent = message;
  els.notice.className = `notice ${type === "error" ? "error" : ""}`;
  window.clearTimeout(showNotice.timer);
  showNotice.timer = window.setTimeout(() => els.notice.classList.add("hidden"), 4200);
}

function setBackendStatus(isOnline) {
  els.statusDot.className = `status-dot ${isOnline ? "online" : "offline"}`;
  els.connectionText.textContent = isOnline ? "Backend connected" : "Backend unavailable";
}

function getCompanyName(companyId) {
  const company = state.companies.find((item) => Number(item.id) === Number(companyId));
  return company ? company.name : `Company #${companyId || "unknown"}`;
}

function normalizeJobCompanyId(job) {
  return job.company?.id || job.companyId || job.CompanyId || "";
}

function escapeHtml(value = "") {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function renderCompanyOptions() {
  const options = state.companies
    .map((company) => `<option value="${company.id}">${escapeHtml(company.name)}</option>`)
    .join("");

  const placeholder = `<option value="">Select company</option>`;
  els.jobCompany.innerHTML = placeholder + options;
  els.reviewCompany.innerHTML = placeholder + options;
  els.reviewCompanyFilter.innerHTML = placeholder + options;

  if (state.selectedReviewCompanyId) {
    els.reviewCompanyFilter.value = state.selectedReviewCompanyId;
  }
}

function renderSummary() {
  els.jobCount.textContent = state.jobs.length;
  els.companyCount.textContent = state.companies.length;
  const ratings = state.companies.map((company) => Number(company.rating)).filter((rating) => !Number.isNaN(rating));
  const average = ratings.length ? ratings.reduce((sum, rating) => sum + rating, 0) / ratings.length : 0;
  els.ratingAverage.textContent = average.toFixed(1);
}

function renderJobs() {
  if (!state.jobs.length) {
    els.jobsList.innerHTML = `<div class="empty">No jobs yet. Create the first role from the form.</div>`;
    return;
  }

  els.jobsList.innerHTML = state.jobs
    .map((job) => {
      const companyId = normalizeJobCompanyId(job);
      const companyName = job.company?.name || getCompanyName(companyId);
      const reviewCount = Array.isArray(job.reviews) ? job.reviews.length : 0;
      return `
        <article class="item">
          <div class="item-head">
            <p class="item-title">${escapeHtml(job.title)}</p>
            <span class="badge">${reviewCount} reviews</span>
          </div>
          <p class="item-meta">${escapeHtml(companyName)} · ${escapeHtml(job.location || "Remote")} · ${escapeHtml(job.minsalary || "N/A")} - ${escapeHtml(job.maxsalary || "N/A")}</p>
          <p class="item-description">${escapeHtml(job.description)}</p>
          <div class="item-actions">
            <button type="button" data-action="edit-job" data-id="${job.id}">Edit</button>
            <button type="button" class="danger" data-action="delete-job" data-id="${job.id}">Delete</button>
          </div>
        </article>
      `;
    })
    .join("");
}

function renderCompanies() {
  if (!state.companies.length) {
    els.companiesList.innerHTML = `<div class="empty">No companies yet. Add one to start posting jobs.</div>`;
    return;
  }

  els.companiesList.innerHTML = state.companies
    .map((company) => `
      <article class="item">
        <div class="item-head">
          <p class="item-title">${escapeHtml(company.name)}</p>
          <span class="badge">${Number(company.rating || 0).toFixed(1)}</span>
        </div>
        <p class="item-description">${escapeHtml(company.description)}</p>
        <div class="item-actions">
          <button type="button" data-action="edit-company" data-id="${company.id}">Edit</button>
          <button type="button" class="danger" data-action="delete-company" data-id="${company.id}">Delete</button>
        </div>
      </article>
    `)
    .join("");
}

function renderReviews() {
  if (!state.selectedReviewCompanyId) {
    els.reviewsList.innerHTML = `<div class="empty">Choose a company to view reviews.</div>`;
    return;
  }

  if (!state.reviews.length) {
    els.reviewsList.innerHTML = `<div class="empty">No reviews for this company yet.</div>`;
    return;
  }

  els.reviewsList.innerHTML = state.reviews
    .map((review) => `
      <article class="item">
        <div class="item-head">
          <p class="item-title">${escapeHtml(review.title)}</p>
          <span class="badge">${Number(review.rating || 0).toFixed(1)}</span>
        </div>
        <p class="item-meta">${escapeHtml(getCompanyName(review.companyId))}</p>
        <p class="item-description">${escapeHtml(review.description)}</p>
        <div class="item-actions">
          <button type="button" data-action="edit-review" data-id="${review.id}">Edit</button>
          <button type="button" class="danger" data-action="delete-review" data-id="${review.id}">Delete</button>
        </div>
      </article>
    `)
    .join("");
}

function renderAll() {
  renderCompanyOptions();
  renderSummary();
  renderJobs();
  renderCompanies();
  renderReviews();
}

async function loadCompanies() {
  state.companies = await request("/companies");
  if (!state.selectedReviewCompanyId && state.companies.length) {
    state.selectedReviewCompanyId = String(state.companies[0].id);
  }
}

async function loadJobs() {
  state.jobs = await request("/jobs");
}

async function loadReviews() {
  if (!state.selectedReviewCompanyId) {
    state.reviews = [];
    return;
  }
  state.reviews = await request(`/reviews?companyId=${encodeURIComponent(state.selectedReviewCompanyId)}`);
}

async function refreshData() {
  try {
    await loadCompanies();
    await Promise.all([loadJobs(), loadReviews()]);
    setBackendStatus(true);
    renderAll();
  } catch (error) {
    setBackendStatus(false);
    showNotice(error.message, "error");
    renderAll();
  }
}

function resetJobForm() {
  els.jobForm.reset();
  $("#job-id").value = "";
  $("#job-form-title").textContent = "Create Job";
  $("#cancel-job-edit").classList.add("hidden");
}

function resetCompanyForm() {
  els.companyForm.reset();
  $("#company-id").value = "";
  $("#company-form-title").textContent = "Create Company";
  $("#cancel-company-edit").classList.add("hidden");
}

function resetReviewForm() {
  els.reviewForm.reset();
  $("#review-id").value = "";
  $("#review-form-title").textContent = "Create Review";
  $("#cancel-review-edit").classList.add("hidden");
}

function editJob(id) {
  const job = state.jobs.find((item) => Number(item.id) === Number(id));
  if (!job) return;
  $("#job-id").value = job.id;
  $("#job-title").value = job.title || "";
  $("#job-company").value = normalizeJobCompanyId(job);
  $("#job-location").value = job.location || "";
  $("#job-minsalary").value = job.minsalary || "";
  $("#job-maxsalary").value = job.maxsalary || "";
  $("#job-description").value = job.description || "";
  $("#job-form-title").textContent = "Edit Job";
  $("#cancel-job-edit").classList.remove("hidden");
}

function editCompany(id) {
  const company = state.companies.find((item) => Number(item.id) === Number(id));
  if (!company) return;
  $("#company-id").value = company.id;
  $("#company-name").value = company.name || "";
  $("#company-rating").value = company.rating || "";
  $("#company-description").value = company.description || "";
  $("#company-form-title").textContent = "Edit Company";
  $("#cancel-company-edit").classList.remove("hidden");
}

function editReview(id) {
  const review = state.reviews.find((item) => Number(item.id) === Number(id));
  if (!review) return;
  $("#review-id").value = review.id;
  $("#review-company").value = review.companyId || state.selectedReviewCompanyId;
  $("#review-title").value = review.title || "";
  $("#review-rating").value = review.rating || "";
  $("#review-description").value = review.description || "";
  $("#review-form-title").textContent = "Edit Review";
  $("#cancel-review-edit").classList.remove("hidden");
}

document.querySelectorAll(".nav-tab").forEach((tab) => {
  tab.addEventListener("click", () => {
    document.querySelectorAll(".nav-tab").forEach((item) => item.classList.remove("active"));
    document.querySelectorAll(".content-view").forEach((item) => item.classList.remove("active"));
    tab.classList.add("active");
    $(`#${tab.dataset.view}-view`).classList.add("active");
    els.viewTitle.textContent = tab.textContent;
  });
});

els.saveApi.addEventListener("click", () => {
  state.apiBase = els.apiBase.value.trim() || "http://localhost:8084";
  localStorage.setItem("jobAppApiBase", state.apiBase);
  refreshData();
});

$("#refresh-jobs").addEventListener("click", refreshData);
$("#refresh-companies").addEventListener("click", refreshData);
$("#cancel-job-edit").addEventListener("click", resetJobForm);
$("#cancel-company-edit").addEventListener("click", resetCompanyForm);
$("#cancel-review-edit").addEventListener("click", resetReviewForm);

els.reviewCompanyFilter.addEventListener("change", async (event) => {
  state.selectedReviewCompanyId = event.target.value;
  await loadReviews();
  renderReviews();
});

els.jobForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const id = $("#job-id").value;
  const payload = {
    title: $("#job-title").value,
    description: $("#job-description").value,
    minsalary: $("#job-minsalary").value,
    maxsalary: $("#job-maxsalary").value,
    location: $("#job-location").value,
    companyId: Number($("#job-company").value),
  };

  try {
    await request(id ? `/jobs/${id}` : "/jobs", {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(payload),
    });
    showNotice(id ? "Job updated." : "Job created.");
    resetJobForm();
    await refreshData();
  } catch (error) {
    showNotice(error.message, "error");
  }
});

els.companyForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const id = $("#company-id").value;
  const payload = {
    name: $("#company-name").value,
    description: $("#company-description").value,
    rating: Number($("#company-rating").value || 0),
  };

  try {
    await request(id ? `/companies/${id}` : "/companies", {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(payload),
    });
    showNotice(id ? "Company updated." : "Company created.");
    resetCompanyForm();
    await refreshData();
  } catch (error) {
    showNotice(error.message, "error");
  }
});

els.reviewForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const id = $("#review-id").value;
  const companyId = $("#review-company").value;
  const payload = {
    title: $("#review-title").value,
    description: $("#review-description").value,
    rating: Number($("#review-rating").value),
    companyId: Number(companyId),
  };

  try {
    await request(id ? `/reviews/${id}` : `/reviews?companyId=${encodeURIComponent(companyId)}`, {
      method: id ? "PUT" : "POST",
      body: JSON.stringify(payload),
    });
    state.selectedReviewCompanyId = String(companyId);
    showNotice(id ? "Review updated." : "Review created.");
    resetReviewForm();
    await refreshData();
  } catch (error) {
    showNotice(error.message, "error");
  }
});

document.body.addEventListener("click", async (event) => {
  const button = event.target.closest("button[data-action]");
  if (!button) return;

  const { action, id } = button.dataset;
  try {
    if (action === "edit-job") editJob(id);
    if (action === "edit-company") editCompany(id);
    if (action === "edit-review") editReview(id);
    if (action === "delete-job") {
      await request(`/jobs/${id}`, { method: "DELETE" });
      showNotice("Job deleted.");
      await refreshData();
    }
    if (action === "delete-company") {
      await request(`/companies/${id}`, { method: "DELETE" });
      showNotice("Company deleted.");
      await refreshData();
    }
    if (action === "delete-review") {
      await request(`/reviews/${id}`, { method: "DELETE" });
      showNotice("Review deleted.");
      await refreshData();
    }
  } catch (error) {
    showNotice(error.message, "error");
  }
});

refreshData();
