# Move HTML files to templates
Move-Item -Path "src\main\java\org\project\oopjava\web_dev_project\*.html" -Destination "src\main\resources\templates\" -Force

# Move CSS, JS, and image files to static
Move-Item -Path "src\main\java\org\project\oopjava\web_dev_project\*.css" -Destination "src\main\resources\static\" -Force
Move-Item -Path "src\main\java\org\project\oopjava\web_dev_project\*.js" -Destination "src\main\resources\static\" -Force
Move-Item -Path "src\main\java\org\project\oopjava\web_dev_project\*.jpg" -Destination "src\main\resources\static\" -Force
Move-Item -Path "src\main\java\org\project\oopjava\web_dev_project\*.jpeg" -Destination "src\main\resources\static\" -Force 