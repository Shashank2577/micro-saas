#!/usr/bin/env bash
set -e

echo "🔧 Setting up micro-saas development environment for Jules..."

# Detect OS
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    OS="linux"
else
    OS="macos"
fi

echo "📦 OS: $OS"

# Install Java 21
echo "☕ Installing Java 21..."
if [ "$OS" == "linux" ]; then
    apt-get update -qq
    apt-get install -y openjdk-21-jdk
    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
else
    brew install openjdk@21
    export JAVA_HOME=$(/usr/libexec/java_home -v 21)
fi
export PATH=$JAVA_HOME/bin:$PATH

# Install Maven 3.9
echo "📦 Installing Maven 3.9..."
if [ "$OS" == "linux" ]; then
    wget -q https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
    tar -xzf apache-maven-3.9.9-bin.tar.gz
    export M2_HOME=$PWD/apache-maven-3.9.9
else
    brew install maven
    export M2_HOME=/usr/local/opt/maven
fi
export PATH=$M2_HOME/bin:$PATH

# Install Node.js 20
echo "📦 Installing Node.js 20..."
if [ "$OS" == "linux" ]; then
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash -
    apt-get install -y nodejs
else
    brew install node@20
fi

# Install cc-starter to local Maven
echo "🔧 Installing cc-starter..."
if [ -d "/app/cross-cutting" ]; then
    cd /app/cross-cutting
    mvn install -DskipTests -q
    echo "✅ cc-starter installed"
else
    echo "⚠️  cross-cutting not found, skipping cc-starter install"
fi

# Verify installations
echo ""
echo "✅ Verifying installations..."
java -version
mvn -version
node --version
npm --version

echo ""
echo "✅ Setup complete! Ready to build micro-saas apps."
