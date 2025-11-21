# 🚀 Deployment Guide

## Prerequisites

Before deploying, ensure you have:

-   ✅ Node.js 18+ installed
-   ✅ Backend API deployed and accessible
-   ✅ Environment variables configured
-   ✅ SSL certificate (for production)

## Local Development

### 1. Clone and Install

```bash
cd d:\Projects\Web\app\frontend
npm install
```

### 2. Configure Environment

```bash
# Copy example file
cp .env.example .env.local

# Edit .env.local
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

### 3. Run Development Server

```bash
npm run dev
```

Visit: http://localhost:3000

## Production Build

### 1. Build the Application

```bash
npm run build
```

This creates an optimized production build in `.next/`

### 2. Test Production Build Locally

```bash
npm start
```

### 3. Environment Variables for Production

```env
NEXT_PUBLIC_API_BASE_URL=https://api.yourdomain.com/api/v1
```

## Deployment Options

### Option 1: Vercel (Recommended for Next.js)

1. **Install Vercel CLI**

```bash
npm i -g vercel
```

2. **Login to Vercel**

```bash
vercel login
```

3. **Deploy**

```bash
vercel
```

4. **Set Environment Variables**

-   Go to Vercel Dashboard
-   Select your project
-   Settings → Environment Variables
-   Add: `NEXT_PUBLIC_API_BASE_URL`

5. **Deploy to Production**

```bash
vercel --prod
```

### Option 2: Docker

1. **Create Dockerfile**

```dockerfile
FROM node:18-alpine AS deps
WORKDIR /app
COPY package*.json ./
RUN npm ci

FROM node:18-alpine AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
ENV NEXT_PUBLIC_API_BASE_URL=http://your-api-url/api/v1
RUN npm run build

FROM node:18-alpine AS runner
WORKDIR /app
ENV NODE_ENV production
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
EXPOSE 3000
ENV PORT 3000
CMD ["node", "server.js"]
```

2. **Build Image**

```bash
docker build -t ums-frontend .
```

3. **Run Container**

```bash
docker run -p 3000:3000 -e NEXT_PUBLIC_API_BASE_URL=http://api-url/api/v1 ums-frontend
```

### Option 3: Traditional Server (Node.js)

1. **Build the application**

```bash
npm run build
```

2. **Copy files to server**

```bash
scp -r .next package*.json user@server:/path/to/app
```

3. **On the server**

```bash
cd /path/to/app
npm ci --production
npm start
```

4. **Use PM2 for process management**

```bash
npm install -g pm2
pm2 start npm --name "ums-frontend" -- start
pm2 save
pm2 startup
```

### Option 4: Static Export (Limited functionality)

Note: Some features may not work in static export mode.

1. **Update next.config.ts**

```typescript
const nextConfig = {
    output: 'export',
};
```

2. **Build**

```bash
npm run build
```

3. **Deploy `out/` folder to any static host**

-   Netlify
-   GitHub Pages
-   AWS S3
-   Any CDN

## Nginx Configuration (Reverse Proxy)

```nginx
server {
    listen 80;
    server_name yourdomain.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

## SSL/HTTPS Setup

### Using Let's Encrypt (Certbot)

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx

# Get certificate
sudo certbot --nginx -d yourdomain.com

# Auto-renewal
sudo certbot renew --dry-run
```

## Environment Variables

### Required Variables

```env
NEXT_PUBLIC_API_BASE_URL=https://api.yourdomain.com/api/v1
```

### Optional Variables

```env
NODE_ENV=production
PORT=3000
```

## Post-Deployment Checklist

-   [ ] Application loads correctly
-   [ ] Login functionality works
-   [ ] API calls are successful
-   [ ] All pages are accessible
-   [ ] HTTPS is working
-   [ ] Environment variables are set
-   [ ] Error logging is configured
-   [ ] Performance is acceptable
-   [ ] Mobile responsiveness works
-   [ ] CORS is configured on backend

## Monitoring

### Vercel Analytics

Add to your project:

```bash
npm install @vercel/analytics
```

In `app/layout.tsx`:

```typescript
import { Analytics } from '@vercel/analytics/react';

export default function RootLayout({ children }) {
    return (
        <html>
            <body>
                {children}
                <Analytics />
            </body>
        </html>
    );
}
```

### Error Tracking (Sentry)

1. **Install Sentry**

```bash
npm install @sentry/nextjs
```

2. **Initialize**

```bash
npx @sentry/wizard@latest -i nextjs
```

## Performance Optimization

### Image Optimization

-   Use Next.js Image component
-   Set proper width/height
-   Use appropriate formats (WebP)

### Code Splitting

-   Next.js handles this automatically
-   Use dynamic imports for heavy components

### Caching

-   Configure proper cache headers
-   Use CDN for static assets

## Troubleshooting

### Build Fails

```bash
# Clear cache
rm -rf .next
npm run build
```

### Environment Variables Not Working

-   Ensure variables start with `NEXT_PUBLIC_`
-   Restart dev server after changes
-   Check build logs

### API Connection Issues

-   Verify CORS settings on backend
-   Check API URL in environment
-   Verify network access

### 404 Errors

-   Ensure proper routing
-   Check file names match routes
-   Verify build completed successfully

## Rollback Strategy

### Vercel

-   Go to deployments
-   Select previous deployment
-   Click "Promote to Production"

### Docker

```bash
# Tag versions
docker tag ums-frontend:latest ums-frontend:v1.0.0

# Rollback
docker stop ums-frontend
docker run -d --name ums-frontend ums-frontend:v1.0.0
```

### PM2

```bash
# Save current state
pm2 save

# Rollback
git checkout previous-commit
npm run build
pm2 restart ums-frontend
```

## Backup Strategy

### Database (if using)

```bash
# Backup
pg_dump dbname > backup.sql

# Restore
psql dbname < backup.sql
```

### Files

```bash
# Backup
tar -czf backup-$(date +%Y%m%d).tar.gz /path/to/app

# Restore
tar -xzf backup-20250101.tar.gz -C /path/to/app
```

## Security Checklist

-   [ ] HTTPS enabled
-   [ ] Environment variables secured
-   [ ] API keys not in code
-   [ ] CORS properly configured
-   [ ] Rate limiting on API
-   [ ] Input validation
-   [ ] XSS protection
-   [ ] CSRF protection
-   [ ] Secure headers configured

## Support

For deployment issues:

1. Check deployment logs
2. Verify environment variables
3. Test API connectivity
4. Review error messages
5. Check browser console

## Additional Resources

-   [Next.js Deployment](https://nextjs.org/docs/deployment)
-   [Vercel Documentation](https://vercel.com/docs)
-   [Docker Documentation](https://docs.docker.com)
-   [PM2 Documentation](https://pm2.keymetrics.io)
