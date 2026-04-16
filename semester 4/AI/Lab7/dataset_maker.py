from datasets import load_from_disk, Dataset
import cv2
import numpy as np

# https://huggingface.co/datasets/Parveshiiii/AI-vs-Real
raw_dataset = load_from_disk("./dataset")

images = []
labels = []

sepia = np.array([[0.393, 0.769, 0.189],
                  [0.349, 0.686, 0.168],
                  [0.272, 0.534, 0.131]])

for example in raw_dataset["train"]:
    if len(images) == 500:
        break

    img = example["image"]  # PIL Image

    # img = img.resize((64, 64)) # i didn't do this, YOU SHOULD! it will help you later when you train the model

    # Convert PIL -> numpy for cv2
    img_np = np.array(img.convert("RGB"))

    # Original image - label 1 (no filter)
    images.append(img_np)
    labels.append(1)

    # Apply sepia and clip values to valid range
    filtered = cv2.transform(img_np, sepia)
    filtered = np.clip(filtered, 0, 255).astype(np.uint8)
    images.append(filtered)
    labels.append(0)

new_dataset = Dataset.from_dict({"image": images, "label": labels})
new_dataset.save_to_disk("./filtered_dataset")

print(f"Done! Total images: {len(images)}")