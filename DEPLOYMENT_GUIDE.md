# 🚀 100% Free Full-Stack Deployment Guide

This guide explains how to deploy the entire **WanderLust** full-stack travel website (Spring Boot Backend + React Frontend + H2 Database) to the internet **100% free of cost** with zero credit card required.

---

## Architecture Overview for Free Hosting

| Component | Technology | Free Host | Cost | Why This Host? |
| :--- | :--- | :--- | :--- | :--- |
| **Frontend** | React 18 + Vite + Tailwind | **[Vercel](https://vercel.com)** | **$0 / month (Free Forever)** | Instant builds, global CDN, auto-SSL, handles client-side routing |
| **Backend** | Spring Boot 3.3.4 (Java 17) | **[Render.com](https://render.com)** | **$0 / month (Free Tier)** | Native Docker deployment, auto-HTTPS, free public URL |
| **Database** | H2 Embedded In-Memory/File | Built into Backend | **$0** | Zero separate DB hosting costs |

---

## Step 1: Push Project to GitHub

1. If not already pushed, create a repository on [GitHub](https://github.com/new) (e.g., `wanderlust-travel-app`).
2. Run these commands in your project terminal:
   ```bash
   git add .
   git commit -m "Configure free deployment for Vercel and Render"
   git branch -M main
   git remote add origin https://github.com/<YOUR_GITHUB_USERNAME>/wanderlust-travel-app.git
   git push -u origin main
   ```

---

## Step 2: Deploy Backend to Render.com (100% Free)

1. Sign up / Log in to **[Render.com](https://render.com)** using your GitHub account.
2. Click **"New +"** in the top right and select **"Web Service"**.
3. Choose **"Build and deploy from a Git repository"** and connect your `wanderlust-travel-app` repository.
4. Fill in the service configuration:
   - **Name:** `wanderlust-backend` (or your preferred name)
   - **Region:** Any (e.g., `Oregon (US West)` or `Frankfurt (EU)`)
   - **Language / Runtime:** Select **Docker**
   - **Dockerfile Path:** `./backend/Dockerfile`
   - **Docker Context:** `./backend`
   - **Instance Type:** Select **Free** ($0/month)
5. Under **Environment Variables**, add:
   - `PORT`: `8080`
   - `APP_CORS_ALLOWED_ORIGINS`: `*`
6. Click **"Create Web Service"**.
7. Render will build the Docker container and start your Spring Boot API. Once finished (typically 2–3 minutes), Render will give you a public URL like:
   `https://wanderlust-backend-xxxx.onrender.com`
8. Verify it works by opening in your browser:
   `https://wanderlust-backend-xxxx.onrender.com/api/destinations`

---

## Step 3: Deploy Frontend to Vercel (100% Free)

1. Sign up / Log in to **[Vercel.com](https://vercel.com)** with your GitHub account.
2. Click **"Add New..."** -> **"Project"**.
3. Import your `wanderlust-travel-app` repository.
4. In the Project Configuration:
   - **Framework Preset:** Vite
   - **Root Directory:** Click **Edit** and select `frontend`
   - **Build Command:** `npm run build`
   - **Output Directory:** `dist`
5. Expand **Environment Variables** and add:
   - **Name:** `VITE_API_URL`
   - **Value:** `https://wanderlust-backend-xxxx.onrender.com/api` *(replace with your actual Render URL from Step 2)*
   - **Name:** `VITE_WS_URL`
   - **Value:** `https://wanderlust-backend-xxxx.onrender.com` *(replace with your actual Render URL from Step 2)*
6. Click **"Deploy"**.
7. Vercel will build and deploy your React frontend in under 45 seconds and give you a free live URL:
   `https://wanderlust-travel-xxxx.vercel.app`

---

## Alternative Free Hosting Options

If you prefer different providers, you can also use:
- **Frontend Alternatives:** [Netlify](https://netlify.com) (Drag-and-drop `frontend/dist` folder or connect GitHub) or [Cloudflare Pages](https://pages.cloudflare.com) (Unlimited free bandwidth).
- **Backend Alternatives:** [Koyeb](https://koyeb.com) (Free Docker container tier, very fast) or [Railway](https://railway.app).

---

## Summary of Free Setup
- **Your Frontend Live URL:** `https://your-app.vercel.app`
- **Your Backend API Live URL:** `https://your-backend.onrender.com/api`
- **Total Monthly Cost:** **$0.00**
