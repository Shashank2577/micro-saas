/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:8154/:path*' // Proxy to backend
      }
    ]
  }
}

module.exports = nextConfig
