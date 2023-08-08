import os

def find_non_empty_packages(directory):
    non_empty_packages = []

    i = 0
    for root, dirs, files in os.walk(directory):
        package_name = os.path.relpath(root, directory).replace(os.path.sep, '.')

        if any(f.endswith(".java") for f in files):
            if i == 0 or i == 1:
                continue
            non_empty_packages.append(package_name)
        i+=1
    return non_empty_packages

def generate_module_info(packages):
    exports = "\n".join(f"    exports {pkg};" for pkg in packages)
    module_info_content = f"module org.JE.JE2 {{\n{exports}\n}}"

    return module_info_content

def write_module_info(content, output_path):
    with open(output_path, 'w') as file:
        file.write(content)

def main():
    directory = "src/main/java/"
    output_path = "src/main/java/module-info.java"

    non_empty_packages = find_non_empty_packages(directory)
    module_info_content = generate_module_info(non_empty_packages)
    write_module_info(module_info_content, output_path)

if __name__ == "__main__":
    main()