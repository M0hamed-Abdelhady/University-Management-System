# Deploying Spring Boot Backend to Railway

## Prerequisites
- GitHub repository with your code
- External PostgreSQL database (Supabase) - already configured in `.env`
- Frontend hosted separately
- Railway account ([railway.app](https://railway.app))

## Step 1: Prepare Your Application

### 1.1 Update CORS Configuration
Add your frontend URL to `SecurityConfig.java` (line 73):

```java
// Production origin - Add your frontend URL here when deploying
configuration.addAllowedOrigin("https://your-frontend-domain.com");
```

### 1.2 Verify Configuration Files
Your application is already configured correctly:
- ✅ Dockerfile ready for production
- ✅ Database credentials in `.env` (will be copied to Railway)
- ✅ Production profile active in `application.yaml`
- ✅ Flyway migrations configured

## Step 2: Deploy to Railway

### 2.1 Create Railway Project

**Option A: Deploy from GitHub (Recommended)**

1. Go to [Railway Dashboard](https://railway.app/dashboard)
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Authenticate with GitHub and select: `M0hamed-Abdelhady/University-Management-System`
5. Railway will detect your repository

**Option B: Deploy with Railway CLI**

```bash
# Install Railway CLI
npm i -g @railway/cli

# Login to Railway
railway login

# Navigate to server directory
cd server

# Initialize and deploy
railway init
railway up
```

### 2.2 Configure Project Settings

1. After project creation, Railway will show your service
2. Click on the service to configure it

**Service Settings:**
- **Name**: `university-backend` (or your preferred name)
- **Root Directory**: Leave empty (Railway auto-detects)
- **Builder**: Dockerfile (auto-detected)

### 2.3 Set Environment Variables

Click **"Variables"** tab and add these:

| Variable | Value |
|----------|-------|
| `DB_URL` | `jdbc:postgresql://aws-1-eu-west-2.pooler.supabase.com:5432/postgres` |
| `DB_USERNAME` | `postgres.gnnvvhelnjcdyteztwuu` |
| `DB_PASSWORD` | `83-rmyLinFhdr%h` |
| `JWT_SECRET_KEY` | `406a970f5fdd329ef30bb1b0e6205f7692d69647322a926bd30ac84ab1c6b8fa` |
| `JWT_EXPIRATION_TIME` | `36000000` |
| `PORT` | `8080` |

**Quick Copy Method:**
You can bulk add variables using Railway's "Raw Editor":
```
DB_URL=jdbc:postgresql://aws-1-eu-west-2.pooler.supabase.com:5432/postgres
DB_USERNAME=postgres.gnnvvhelnjcdyteztwuu
DB_PASSWORD=83-rmyLinFhdr%h
JWT_SECRET_KEY=406a970f5fdd329ef30bb1b0e6205f7692d69647322a926bd30ac84ab1c6b8fa
JWT_EXPIRATION_TIME=36000000
PORT=8080
```

### 2.4 Configure Networking

1. Go to **"Settings"** tab
2. Scroll to **"Networking"** section
3. Click **"Generate Domain"** to get a public URL
4. Your service will be available at: `https://your-service.up.railway.app`

**Note:** Railway provides HTTPS automatically!

## Step 3: Configure Build Settings (If Needed)

Railway auto-detects your Dockerfile, but if you need custom settings:

1. Go to **"Settings"** → **"Build"**
2. Set **Root Directory**: `server` (if deploying from monorepo root)
3. **Build Command**: (leave empty, Dockerfile handles it)
4. **Start Command**: (leave empty, Dockerfile handles it)

## Step 4: Deploy

Railway automatically deploys when you:
- Push to your connected branch (usually `master`)
- Click **"Deploy"** in the Railway dashboard

**First Deployment:**
1. Click **"Deploy"** button
2. Watch the build logs in real-time
3. Wait for build to complete (~3-5 minutes)
4. Service will be available at your Railway domain

## Step 5: Post-Deployment

### 5.1 Verify Deployment

Check these endpoints:
- `https://your-service.up.railway.app/swagger-ui.html` - API Documentation
- `https://your-service.up.railway.app/v3/api-docs` - OpenAPI Spec
- `https://your-service.up.railway.app/api/auth/login` - Should return 405 (Method Not Allowed) for GET

### 5.2 Update Frontend API URL

Update your frontend configuration:

**Option 1: Environment Variable (Recommended)**
```bash
# In your frontend .env or .env.production
NEXT_PUBLIC_API_URL=https://your-service.up.railway.app
```

**Option 2: Direct Configuration**
```typescript
// In frontend/lib/api.ts
const API_BASE_URL = 'https://your-service.up.railway.app';
```

### 5.3 Update CORS

1. Edit `SecurityConfig.java` and add your frontend domain:
   ```java
   configuration.addAllowedOrigin("https://your-frontend-domain.com");
   ```
2. Commit and push changes
3. Railway auto-redeploys

### 5.4 Test Integration

1. Open your frontend
2. Try logging in
3. Test API calls
4. Check browser console for CORS errors

## Step 6: Database Migration

Flyway migrations run automatically on startup:
- Check deployment logs to confirm migrations succeeded
- Look for: `Successfully applied X migration(s)`

## Railway Features

### Automatic Deployments
- Every git push triggers a new deployment
- Railway shows deployment status in real-time
- Automatic rollback if deployment fails

### Environment Management
- Create multiple environments (staging, production)
- Copy environment variables between environments
- Branch-based deployments

### Monitoring
- View logs in real-time
- Monitor CPU and memory usage
- Track deployment history
- Set up usage alerts

### CLI Commands
```bash
# View logs
railway logs

# Open service in browser
railway open

# Link to different project
railway link

# View environment variables
railway variables

# Run commands in Railway environment
railway run <command>
```

## Troubleshooting

### Common Issues

**1. Build Fails**
```bash
# Check logs
railway logs

# Common fixes:
- Ensure mvnw has execute permissions (check git settings)
- Verify Java 21 is specified in Dockerfile
- Check for Maven build errors
```

**2. Database Connection Fails**
- Verify environment variables are set correctly
- Check Supabase allows Railway IPs (usually no restrictions needed)
- Test connection string format

**3. Port Issues**
- Railway uses `PORT` environment variable
- Your app should listen on `${PORT}` or 8080
- Check Dockerfile EXPOSE statement

**4. CORS Errors**
- Add frontend domain to `SecurityConfig.java`
- Verify domain is exact match (including https://)
- Check browser developer console for specific error

**5. Service Crashes**
- Check logs for Java exceptions
- Verify JVM memory settings
- Ensure database migrations succeeded

### Viewing Logs

**Dashboard:**
1. Go to your service
2. Click **"Deployments"** tab
3. Click on latest deployment
4. View build and runtime logs

**CLI:**
```bash
railway logs --follow
```

## Configuration Tips

### 1. Custom Domain
1. Go to **"Settings"** → **"Networking"**
2. Click **"Custom Domain"**
3. Add your domain
4. Configure DNS with provided CNAME
5. Update CORS configuration

### 2. Health Checks
Add Spring Boot Actuator (optional):

```xml
<!-- In pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Then configure health endpoint in Railway:
- **Health Check Path**: `/actuator/health`

### 3. Resource Limits
Railway automatically scales resources, but you can:
- View current usage in dashboard
- Set budget alerts
- Upgrade plan for more resources

### 4. Secrets Management
For sensitive values:
1. Use Railway's encrypted variables
2. Never commit `.env` to git (already in `.gitignore`)
3. Rotate JWT secret periodically

## Performance Optimization

### 1. Docker Image Optimization
Your multi-stage Dockerfile is already optimized:
- ✅ Uses Alpine Linux (smaller image)
- ✅ Separate build and runtime stages
- ✅ JRE instead of JDK in final image

### 2. JVM Settings (Optional)
Add to Dockerfile if needed:
```dockerfile
ENV JAVA_OPTS="-Xmx512m -Xms256m"
```

### 3. Connection Pool
Your database connection pool is configured in application.yaml.
Monitor connections in Railway dashboard.

## Cost & Limits

**Hobby Plan (Free $5 credit/month):**
- Enough for small projects
- No credit card required
- Shared resources
- Automatic sleep after inactivity (optional)

**Developer Plan ($5/month):**
- $5 credit included
- Better performance
- Priority support
- No automatic sleep

**Pricing:**
- Pay only for what you use
- ~$5-10/month for typical Spring Boot app
- Monitor usage in dashboard

## CI/CD Best Practices

### 1. Branch Deployments
- `master` → Production
- `develop` → Staging environment
- Feature branches → Preview deployments

### 2. Rollbacks
If deployment fails:
1. Go to **"Deployments"**
2. Find previous working deployment
3. Click **"Rollback"**

### 3. Testing Before Deploy
```bash
# Test locally with Railway environment
railway run ./mvnw spring-boot:run

# Or run tests
railway run ./mvnw test
```

## Security Checklist

- ✅ Environment variables set (not hardcoded)
- ✅ JWT secret is secure (256-bit key)
- ✅ Database credentials protected
- ✅ CORS configured for specific origins only
- ✅ HTTPS enabled (automatic on Railway)
- ✅ `.env` file in `.gitignore`
- ✅ Regular dependency updates

## Monitoring & Alerts

### 1. Set Up Alerts
1. Go to project settings
2. Configure usage alerts
3. Set budget limits

### 2. Logging
```bash
# Tail logs
railway logs -f

# Filter logs
railway logs | grep ERROR
```

### 3. Metrics
Monitor in dashboard:
- CPU usage
- Memory usage
- Network traffic
- Request count

## Next Steps

1. ✅ Deploy to Railway
2. ✅ Get your service URL
3. ✅ Add frontend domain to CORS
4. ✅ Update frontend API URL
5. ✅ Test all API endpoints
6. ✅ Set up monitoring
7. ✅ Configure custom domain (optional)
8. ✅ Set up staging environment (optional)

## Useful Links

- [Railway Dashboard](https://railway.app/dashboard)
- [Railway Documentation](https://docs.railway.app/)
- [Railway Discord Community](https://discord.gg/railway)
- [Railway Status Page](https://status.railway.app/)

## Quick Reference

### Deployment Checklist
- [ ] Railway account created
- [ ] GitHub repository connected
- [ ] Environment variables configured
- [ ] Service deployed successfully
- [ ] API endpoints accessible
- [ ] Frontend CORS configured
- [ ] Database migrations applied
- [ ] Frontend API URL updated
- [ ] Integration tested

### Environment Variables
```
DB_URL=jdbc:postgresql://aws-1-eu-west-2.pooler.supabase.com:5432/postgres
DB_USERNAME=postgres.gnnvvhelnjcdyteztwuu
DB_PASSWORD=83-rmyLinFhdr%h
JWT_SECRET_KEY=406a970f5fdd329ef30bb1b0e6205f7692d69647322a926bd30ac84ab1c6b8fa
JWT_EXPIRATION_TIME=36000000
PORT=8080
```

### Common Commands
```bash
railway login              # Authenticate
railway link               # Link to project
railway up                 # Deploy
railway logs               # View logs
railway logs -f            # Follow logs
railway open               # Open in browser
railway variables          # List variables
railway status             # Check status
```
