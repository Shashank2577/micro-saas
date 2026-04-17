import * as React from "react"

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "default" | "outline" | "ghost";
  size?: "default" | "sm" | "lg";
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant = "default", size = "default", ...props }, ref) => {
    let variantClass = "bg-slate-900 text-white hover:bg-slate-800";
    if (variant === "outline") variantClass = "border border-slate-200 bg-white hover:bg-slate-100 text-slate-900";
    if (variant === "ghost") variantClass = "hover:bg-slate-100 hover:text-slate-900 text-slate-700";

    let sizeClass = "h-10 px-4 py-2";
    if (size === "sm") sizeClass = "h-9 rounded-md px-3";
    if (size === "lg") sizeClass = "h-11 rounded-md px-8";

    return (
      <button
        ref={ref}
        className={`inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none disabled:pointer-events-none disabled:opacity-50 ${variantClass} ${sizeClass} ${className || ""}`}
        {...props}
      />
    )
  }
)
Button.displayName = "Button"

export { Button }
