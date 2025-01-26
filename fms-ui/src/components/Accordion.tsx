import React, {useRef, useState} from 'react';

interface AccordionProps {
    title: string;
    children: React.ReactNode;
}

const Accordion: React.FC<AccordionProps> = ({title, children}) => {
    const [isOpen, setIsOpen] = useState(false);
    const contentRef = useRef<HTMLDivElement>(null);

    const toggleAccordion = () => {
        setIsOpen((prev) => !prev);
    }

    return (
        <div className="border border-gray-200 rounded-lg shadow-md mb-4">
            <div
                className="flex justify-between items-center p-4 bg-gray-100 hover:bg-gray-200 cursor-pointer transition-colors duration-300 rounded-t-lg"
                onClick={toggleAccordion}
            >
                <h3 className="text-lg font-semibold text-gray-700">{title}</h3>
                <span
                    className={`transform transition-transform duration-300 ${
                        isOpen ? "rotate-180" : "rotate-0"
                    }`}
                >
                    ▼
                </span>
            </div>

            <div
                ref={contentRef}
                className={`transition-all duration-500 overflow-hidden ${
                    isOpen ? "max-h-screen" : "max-h-0"
                }`}
                style={{
                    maxHeight: isOpen
                        ? contentRef.current?.scrollHeight + "px"
                        : "0px",
                }}
            >
                <div className="p-4 bg-white border-t border-gray-200">
                    {children}
                </div>
            </div>
        </div>
    );
};

export default Accordion;