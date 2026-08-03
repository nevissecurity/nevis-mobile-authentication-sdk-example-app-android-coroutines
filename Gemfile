source "https://rubygems.org"

gem "fastlane", "~> 2.225"
gem "java-properties", "~> 0.3.0"

group :development do
    gem "rubocop", "~> 1.82.1", require: false
end

plugins_path = File.join(File.dirname(__FILE__), "fastlane", "Pluginfile")
eval_gemfile(plugins_path) if File.exist?(plugins_path)
